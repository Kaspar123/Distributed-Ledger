package controller;

import enums.StatusCode;
import main.Block;
import main.WebServer;
import util.Utils;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by kaspar on 16.03.18.
 */
public class BlockController implements Controller {

    private final String route;

    public BlockController(String route) {
        this.route = route;
    }

    @Override
    public void doGet(Request request, Response response) {
        PrintWriter out = response.getWriter();
        response.setCode(StatusCode.OK);

        Optional<Block[]> blocks = Utils.JSONtoObject(WebServer.BLOCKS, Block[].class);
        if (!blocks.isPresent()) {
            response.setCode(StatusCode.INTERNAL_SERVER_ERROR);
            return;
        }
        List<Block> blockList = Arrays.asList(blocks.get());

        String content = "";

        if (route.equalsIgnoreCase("/getblocks")) {
            content = getblocks(request, response, blockList);
        }
        else if (route.equalsIgnoreCase("/getdata")) {
            content = getdata(request, response, blockList);
        }

        out.print(response.getVersion() + " ");
        out.println(response.getCode());
        out.println("Content-Type: application/json");
        out.println("Content-Length: " + content.length());
        out.println();

        out.println(content);

    }

    @Override
    public void doPost(Request request, Response response) {

    }

    private String getblocks(Request request, Response response, List<Block> blocks) {
        Map<String, String> params = Utils.splitQuery(request.getQueryString());
        if (params.containsKey("id")) {
            String id = params.get("id");
            Optional<Block> block = blocks.stream().filter(c -> c.getHash().equals(id)).findFirst();
            blocks = (block.isPresent()) ? blocks.subList(blocks.indexOf(block.get()), blocks.size()) : new ArrayList<>();
        }
        // TODO: kui failure, siis voiks mingi symboolne JSON tekkida
        return Utils.objectToJSONString(blocks, true);
    }

    private String getdata(Request request, Response response, List<Block> blocks) {
        Map<String, String> params = Utils.splitQuery(request.getQueryString());
        if (params.containsKey("id")) {
            String id = params.get("id");
            Optional<Block> block = blocks.stream().filter(c -> c.getHash().equals(id)).findAny();
            if (block.isPresent()) return Utils.objectToJSONString(block.get(), true);
        }
        // TODO: in case such block does not exist
        return "";
    }
}
