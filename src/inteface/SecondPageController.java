package inteface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import util.Etat;

import java.net.URL;
import java.util.ResourceBundle;

public class SecondPageController implements Initializable {

    @FXML
    private TableView<ObservableList<Integer>> tableauEtat;

    @FXML
    private TextField alg_select;

    @FXML
    private TextArea Resultarea;

    private Etat etat;

    public void setEtat(Etat etat) {
        this.etat = etat;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alg_select.setEditable(false);
        Resultarea.setEditable(false);
    }

    public void setAlg_select(String alg, long temps, int val) {
        alg_select.setText(alg);
        String text = "Temps d'execution : " + temps/1000 + "ms\nTotal value of Knapsacks: " + val;
        Resultarea.setText(text);
    }

    public void handleTableView () {
        int [][] matrix = etat.getMatrice();

        tableauEtat.getItems().clear();
        tableauEtat.getColumns().clear();

        // Ajouter les colonnes à la TableView
        for (int i = 0; i < matrix[0].length; i++) {
            final int columnIndex = i;
            TableColumn<ObservableList<Integer>, Integer> column = new TableColumn<>("Objet " + (i + 1));
            column.setCellValueFactory(cellData -> {
                ObservableList<Integer> row = cellData.getValue();
                return javafx.beans.binding.Bindings.createObjectBinding(() -> row.get(columnIndex));
            });
            tableauEtat.getColumns().add(column);
        }

        // Ajouter les lignes à la TableView
        for (int[] row : matrix) {
            ObservableList<Integer> rowData = FXCollections.observableArrayList();
            for (int value : row) {
                rowData.add(value);
            }
            tableauEtat.getItems().add(rowData);
        }
    }
}


