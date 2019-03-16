import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Application extends javafx.application.Application {
    HBox hBox = new HBox();
    public static String count(String absolutePath) throws IOException {
        List<Citation> citationList = new ArrayList<>();
        String result = "";
        for (int i = 0; i < 60; i++) {
            citationList.add(new Citation("["+i+"]"));
        }

        try (PDDocument document = PDDocument.load(new File(absolutePath))) {

            document.getClass();

            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);
                //System.out.println("Text:" + st);

                // split by whitespace
                String lines[] = pdfFileInText.split("\\r?\\n");

                for (String line : lines) {
                    for (int i = 1; i < 60; i++) {
                        try {
                            if(line.contains(citationList.get(i).getCiteSymbol())){
                                citationList.get(i).setNumberOfRepetiton(citationList.get(i).getNumberOfRepetiton()+1);
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
//                    System.out.println(line);
                }
                Collections.sort(citationList, new Comparator<Citation>() {
                    @Override
                    public int compare(Citation o1, Citation o2) {
                        if(o1.getNumberOfRepetiton() > o2.getNumberOfRepetiton()) return 1;
                        else return -1;
                    }
                });
                Collections.reverse(citationList);
                for (int i = 0; i < 5; i++) {
                    result = result + citationList.get(i).getCiteSymbol() +": "+citationList.get(i).getNumberOfRepetiton() + "\n";
                };
                return result;
            }

        }
        return "";
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(hBox,1000,300);
        primaryStage.setTitle("Citation counter");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                try {
                    fileDropped(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        scene.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                mouseDragOver(event);
            }
        });
    }

    private void mouseDragOver(DragEvent event) {
        final Dragboard db = event.getDragboard();

        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".pdf");

        if (db.hasFiles()) {
            if (isAccepted) {
                hBox.setStyle("-fx-border-color: red;"
                        + "-fx-border-width: 5;"
                        + "-fx-background-color: #C6C6C6;"
                        + "-fx-border-style: solid;");
                event.acceptTransferModes(TransferMode.COPY);
            }
        } else {
            event.consume();
        }
    }

    private void fileDropped(DragEvent event) throws IOException {
        Dragboard dragboard = event.getDragboard();
        String absolutePath = dragboard.getFiles().get(0).getAbsolutePath();
        String count = count(absolutePath);
        String result = dragboard.getFiles().get(0).getName() + "\n" + count;
        TextArea textArea  = new TextArea(result);
        textArea.setPrefWidth(200);
        hBox.getChildren().add(textArea);
    }
}
