package com.dominion.server;

import com.dominion.common.Constants;
import com.dominion.common.Game;
import com.dominion.common.GameInfo;
import com.dominion.common.Player;
import com.dominion.common.PlayerInfo;
import com.dominion.utils.IOUtils;
import com.dominion.utils.JsonUtils;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.servlet.DefaultServlet;

public class GameViewServlet extends DefaultServlet {

    private static final long serialVersionUID = 7474354125823135067L;

    private final ServerStatus serverStatus;
    private final static Pattern GAMEID = Pattern.compile("/game/[0-9]*");
    private final static String HTML_PATH = "htmls/gameView.html";
    private final static String HTML_STRING = IOUtils.readFile(HTML_PATH, StandardCharsets.UTF_8);

    public GameViewServlet(ServerStatus serverStatus) {
        super();
        this.serverStatus = serverStatus;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String contextInfo = request.getContextPath();
        final String pathInfo = request.getPathInfo();
        System.out.println("get: " + contextInfo + " " + pathInfo + " " + request.getContentType());
        if (request.getContentType() == Constants.JSON_TYPE) {
            if (StringUtils.isEmpty(pathInfo)) {
                response.setContentType("text/html");
                response.getWriter().print("Under construction");
            } else if (GAMEID.matcher(pathInfo).matches()) {
                // list game waiting room
                final int gameId = Integer.parseInt(pathInfo.substring(6));
                System.out.println(gameId);

                if (serverStatus.containsGame(gameId)) {
                    final Game game = serverStatus.getGame(gameId);
                    final GameInfo info = game.getGameInfo();
                    response.setContentType(Constants.JSON_TYPE);
                    response.getWriter().print(JsonUtils.writeJsonObjectToString(info));
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
                response.setContentType("text/html");
                response.getWriter().print(HTML_STRING);
            }
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();
        System.out.println("post: " + pathInfo);

        if (GAMEID.matcher(pathInfo).matches()) {
            // game creating form POST, redirect to waiting room, a view page
            final int gameId = Integer.parseInt(pathInfo.substring(6));
            Preconditions.checkNotNull(request.getParameter("user"));
            Preconditions.checkNotNull(request.getParameter("deck"));

            final String creatorName = request.getParameter("user");
            final String deckName = request.getParameter("deck");

            Game newGame = new Game(gameId, new Player(new PlayerInfo(creatorName)), Constants.popularDeckset.get(deckName));
            serverStatus.addGame(newGame);
            response.sendRedirect("view/game/" + gameId);
        } else {
            // unknown request
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
