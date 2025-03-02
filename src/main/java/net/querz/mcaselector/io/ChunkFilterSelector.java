package net.querz.mcaselector.io;

import javafx.application.Platform;
import net.querz.mcaselector.Config;
import net.querz.mcaselector.filter.GroupFilter;
import net.querz.mcaselector.tiles.TileMap;
import net.querz.mcaselector.ui.ProgressTask;
import net.querz.mcaselector.util.Debug;
import net.querz.mcaselector.util.Helper;
import net.querz.mcaselector.util.Point2i;
import net.querz.mcaselector.util.Timer;
import net.querz.mcaselector.util.Translation;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

public class ChunkFilterSelector {

	private ChunkFilterSelector() {}

	public static void selectFilter(GroupFilter filter, TileMap tileMap, ProgressTask progressChannel) {
		File[] files = Config.getWorldDir().listFiles((d, n) -> n.matches(Helper.MCA_FILE_PATTERN));
		if (files == null || files.length == 0) {
			progressChannel.done(Translation.DIALOG_PROGRESS_NO_FILES.toString());
			return;
		}

		MCAFilePipe.clearQueues();

		progressChannel.setMax(files.length);
		progressChannel.updateProgress(files[0].getName(), 0);

		for (File file : files) {
			MCAFilePipe.addJob(new MCASelectFilterLoadJob(file, filter, tileMap, progressChannel));
		}
	}

	public static class MCASelectFilterLoadJob extends LoadDataJob {

		private GroupFilter filter;
		private ProgressTask progressChannel;
		private TileMap tileMap;

		MCASelectFilterLoadJob(File file, GroupFilter filter, TileMap tileMap, ProgressTask progressChannel) {
			super(file);
			this.filter = filter;
			this.tileMap = tileMap;
			this.progressChannel = progressChannel;
		}

		@Override
		public void execute() {
			Matcher m = Helper.REGION_GROUP_PATTERN.matcher(getFile().getName());
			if (m.find()) {
				int regionX = Integer.parseInt(m.group("regionX"));
				int regionZ = Integer.parseInt(m.group("regionZ"));

				if (!filter.appliesToRegion(new Point2i(regionX, regionZ))) {
					Debug.dump("filter does not apply to file " + getFile().getName());
					progressChannel.incrementProgress(getFile().getName());
					return;
				}

				byte[] data = load();
				if (data != null) {
					MCAFilePipe.executeProcessData(new MCASelectFilterProcessJob(getFile(), data, filter, tileMap, new Point2i(regionX, regionZ), progressChannel));
				} else {
					Debug.errorf("error loading mca file %s", getFile().getName());
					progressChannel.incrementProgress(getFile().getName());
				}
			} else {
				Debug.dump("wtf, how did we get here??");
				progressChannel.incrementProgress(getFile().getName());
			}
		}
	}

	public static class MCASelectFilterProcessJob extends ProcessDataJob {

		private ProgressTask progressChannel;
		private GroupFilter filter;
		private TileMap tileMap;
		private Point2i location;

		MCASelectFilterProcessJob(File file, byte[] data, GroupFilter filter, TileMap tileMap, Point2i location, ProgressTask progressChannel) {
			super(file, data);
			this.filter = filter;
			this.tileMap = tileMap;
			this.location = location;
			this.progressChannel = progressChannel;
		}

		@Override
		public void execute() {
			//load MCAFile
			Timer t = new Timer();
			try {
				MCAFile mca = MCAFile.readAll(getFile(), new ByteArrayPointer(getData()));
				if (mca != null) {
					Set<Point2i> chunks = mca.getFilteredChunks(filter);
					Map<Point2i, Set<Point2i>> region = new HashMap<>();
					region.put(location, chunks);

					Platform.runLater(() -> {
						tileMap.addMarkedChunks(region);
						tileMap.update();
					});
					Debug.dumpf("took %s to delete chunk indices in %s", t, getFile().getName());
				}
			} catch (Exception ex) {
				Debug.errorf("error selecting chunks in %s", getFile().getName());
			}
			progressChannel.incrementProgress(getFile().getName());
		}
	}
}
