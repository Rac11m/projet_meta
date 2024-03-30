package inteface;

        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.ChoiceBox;
        import javafx.scene.control.Spinner;
        import javafx.scene.control.SpinnerValueFactory;
        import javafx.scene.control.TextField;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;

        import java.io.File;
        import java.net.URL;
        import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    private ChoiceBox<String> chcBox;

    @FXML
    private Spinner<Integer> nbr_sac_spinner;

    @FXML
    private Spinner<Integer> nbr_objet_spinner;

    @FXML
    private TextField filePathTextFieldS;

    @FXML
    private TextField filePathTextFieldO;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int minValue = 1;
        int maxValue = 100;

        // Create a list of values
        ObservableList<String> values = FXCollections.observableArrayList("BFS", "DFS", "A*");

        // Set the values to the ChoiceBox
        chcBox.setItems(values);

        SpinnerValueFactory<Integer> valueSacSpinner =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, minValue);
        SpinnerValueFactory<Integer> valueObjetSpinner =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, minValue);

        // Set the SpinnerValueFactory to the Spinner
        nbr_sac_spinner.setValueFactory(valueSacSpinner);
        nbr_objet_spinner.setValueFactory(valueObjetSpinner);
    }

    public String getSelectedAlgorithm() {
        return chcBox.getValue();
    }

    @FXML
    private void execute() {
        String sacsFilePath = filePathTextFieldS.getText();
        String objetsFilePath = filePathTextFieldO.getText();
        String selectedAlgorithm = getSelectedAlgorithm();

        // Passer les informations récupérées à une autre méthode pour traitement
        processExecution(sacsFilePath, objetsFilePath, selectedAlgorithm);
    }

    // Méthode pour effectuer les opérations nécessaires avec les informations récupérées
    private void processExecution(String sacsFilePath, String objetsFilePath, String selectedAlgorithm) {
        // Effectuer les opérations nécessaires avec les informations récupérées
        System.out.println("Chemin du fichier des sacs : " + sacsFilePath);
        System.out.println("Chemin du fichier des objets : " + objetsFilePath);
        System.out.println("Algorithme selectionne : " + selectedAlgorithm);
        if (selectedAlgorithm.equals("BFS")) {
            System.out.println("BFS");
        } else if (selectedAlgorithm.equals("DFS")) {
            System.out.println("DFS");
        } else {
            System.out.println("A*");
        }
    }

    @FXML
    private void chooseFileS() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier CSV");

        // Filtre pour les fichiers CSV
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        // Mettre à jour le champ de texte avec le chemin du fichier sélectionné
        if (selectedFile != null) {
            filePathTextFieldS.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void chooseFileO() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier CSV");

        // Filtre pour les fichiers CSV
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        // Mettre à jour le champ de texte avec le chemin du fichier sélectionné
        if (selectedFile != null) {
            filePathTextFieldO.setText(selectedFile.getAbsolutePath());
        }
    }
}

