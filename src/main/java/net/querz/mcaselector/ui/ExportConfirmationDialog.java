package net.querz.mcaselector.ui;

import javafx.stage.Stage;
import net.querz.mcaselector.tiles.TileMap;
import net.querz.mcaselector.util.Translation;

public class ExportConfirmationDialog extends ConfirmationDialog {

	public ExportConfirmationDialog(TileMap tileMap, Stage primaryStage) {
		super(
				primaryStage,
				Translation.DIALOG_EXPORT_CHUNKS_CONFIRMATION_TITLE,
				Translation.DIALOG_EXPORT_CHUNKS_CONFIRMATION_HEADER_SHORT,
				"export"
		);

		if (tileMap != null) {
			headerTextProperty().unbind();
			setHeaderText(String.format(Translation.DIALOG_EXPORT_CHUNKS_CONFIRMATION_HEADER_VERBOSE.toString(), tileMap.getSelectedChunks()));
		}
	}
}
