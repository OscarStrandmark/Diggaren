import controllers.*;


import static spark.Spark.*;

public class StartAPI {
    public static void main(String[] args) {
        port(5050);

        AddToLibraryController addToLibraryController = new AddToLibraryController();
        path("/spotify", () -> {
        //Search endpoints
            path("/search", () -> {
                get("/", (req,res) -> spotifySearch.search(req.params("auth"),req.params("type"),req.params("query")));
            });
            path("/library", () -> {
                put("/addto", (req,res) -> addToLibraryController.addToLibrary(req.body()));
    }
}
