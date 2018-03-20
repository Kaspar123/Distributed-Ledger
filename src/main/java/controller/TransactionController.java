package controller;

import enums.StatusCode;
import main.Block;
import util.Utils;

import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by kaspar on 18.03.18.
 */
public class TransactionController implements Controller {

    private static Block currentBlock = new Block();
    private static final int MAX_BLOCK_SIZE = 3;

    @Override
    public void doGet(Request request, Response response) {
        PrintWriter out = response.getWriter();
        response.setCode(StatusCode.OK);

        Map<String, String> params = Utils.splitQuery(request.getQueryString());
        String transaction = params.get("transaction");

        if (currentBlock.getTransactions().size() != MAX_BLOCK_SIZE) {

        }

    }

    @Override
    public void doPost(Request request, Response response) {

    }
}
