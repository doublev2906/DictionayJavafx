package sample;

import impl.org.controlsfx.skin.AutoCompletePopup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.control.textfield.AutoCompletionBinding;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    private List<Word> words;

    @FXML
    private TextField en_word;
    @FXML
    private TextArea vn_word;
    @FXML
    private Button button;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initWords();
            ArrayList<String> listEngWord = new ArrayList<>();
            for (var i : words) listEngWord.add(i.getEngWord());
            AutoCompletePopup<String> autoCompletePopup = new AutoCompletePopup<>();
            autoCompletePopup.getSuggestions().addAll(listEngWord);
            autoCompletePopup.setStyle("-fx-control-inner-background:BLACK;"
                    + "-fx-accent: #E8EAF6;"
                    + "-fx-selection-bar-non-focused:red;"
                    + "-fx-font:18px 'Arial'");



            TextFields.bindAutoCompletion(en_word, t -> {
                return listEngWord.stream().filter(elem ->
                {
                    return elem.toLowerCase().startsWith(t.getUserText().toLowerCase());
                }).collect(Collectors.toList());
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Alert");
        alert.setContentText("Sorry nha! Từ này mình chưa thêm vào từ điển :<");
        alert.showAndWait();

    }

    public void translate(ActionEvent event) throws IOException {
        boolean check = true;
        String word = en_word.getText();
        for (Word value : words) {
            if (word.equals(value.getEngWord())) {
                check = false;
                vn_word.setText(value.getVnWord());
                break;
            }
        }
        if (check) {
            showAlert();
        }
    }

    public void initWords() throws IOException {
        File file = new File("data.txt");
        var check = file.exists();
        words = new ArrayList<>();
        int count = 1;
        Scanner readFile = new Scanner(file);
        FileReader fileReader = new FileReader(file);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        Word word = new Word();
        while (lineNumberReader.readLine() != null) {

            var line = readFile.nextLine();
            if (count % 2 == 1) {
                word.setEngWord(line);
            } else {
                word.setVnWord(line);
                words.add(word);
                word = new Word();
            }
            count++;

        }

        readFile.close();
    }
}
