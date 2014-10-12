package com.dominion.server;

import com.dominion.common.Constants;
import com.dominion.common.Game;
import com.dominion.common.PublicCards;
import com.dominion.utils.JsonUtils;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlet.DefaultServlet;

public class GameInfoServlet extends DefaultServlet {

    private static final long serialVersionUID = 7591129997049751313L;
    private final ServerStatus serverStatus;
    private final static Pattern GAME_INFO = Pattern.compile("/game/[0-9]*/[a-z_]*");
    private final static Pattern USER_INFO = Pattern.compile("/game/[0-9]*/usr/[a-zA-Z0-9]*/[a-z]*");

    public GameInfoServlet(ServerStatus serverStatus) {
        super();
        this.serverStatus = serverStatus;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();
        System.out.println(pathInfo);
        if (GAME_INFO.matcher(pathInfo).matches()) {
            String[] params = pathInfo.split("/");
            final int gameId = Integer.parseInt(params[2]);
            final String query = params[3];
            final Game game = serverStatus.getGame(gameId);
            if (query.equals("public_cards")) {
                final PublicCards cards = game.getPublicCards();
                System.out.println(JsonUtils.writeJsonObjectToString(cards));
                response.setContentType(Constants.JSON_TYPE);
                response.getWriter().print(JsonUtils.writeJsonObjectToString(cards));
            } else if (query.equals("public_cards")) {

            } else {
                response.setContentType("text");
                response.getWriter().print("unknown request: " + pathInfo);
            }
        } else if (USER_INFO.matcher(pathInfo).matches()) {
            String[] params = pathInfo.split("/");
            final int gameId = Integer.parseInt(params[2]);
            final String usrName = params[4];
            final String query = params[5];
            if (query.equals("stats")) {

            } else {
                response.setContentType("text");
                response.getWriter().print("unknown request: " + pathInfo);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();

        if (USER_INFO.matcher(pathInfo).matches()) {
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
