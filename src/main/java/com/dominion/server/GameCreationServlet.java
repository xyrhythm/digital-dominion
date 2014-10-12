package com.dominion.server;

import com.dominion.common.Constants;
import com.dominion.common.Game;
import com.dominion.common.Player;
import com.dominion.common.PlayerInfo;
import com.dominion.utils.IOUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.servlet.DefaultServlet;

public class GameCreationServlet extends DefaultServlet {

    private static final long serialVersionUID = 1L;
    private final ServerStatus serverStatus;
    private final static Pattern GAMEID = Pattern.compile("/game/[0-9]*");
    private final static String FORM_PATH = "htmls/gameCreationForm.html";
    private final static String FORM_STRING = IOUtils.readFile(FORM_PATH, StandardCharsets.UTF_8);

    public GameCreationServlet(ServerStatus serverStatus) {
        super();
        this.serverStatus = serverStatus;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();

        if (StringUtils.isEmpty(pathInfo)) {
            // create a game id and redirect
            final int gameId = serverStatus.genGameId();
            response.sendRedirect("create/game/" + gameId);
        } else if (GAMEID.matcher(pathInfo).matches()) {
            // render game creating form, which will POST to the same url
            // final int gameId = Integer.parseInt(pathInfo.substring(6));
            response.setContentType("text/html");
            response.getWriter().print(FORM_STRING);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();

        if (GAMEID.matcher(pathInfo).matches()) {
            // game creating form POST, redirect to waiting room, a view page
            final int gameId = Integer.parseInt(pathInfo.substring(6));
            final String creatorName = request.getParameter("user");
            final String deckName = request.getParameter("deck");

            if (StringUtils.isBlank(creatorName)) {
                // TODO, might have to lean JSP!!!!!
                response.getWriter().println("invalid user name");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                Game newGame = new Game(gameId, new Player(new PlayerInfo(creatorName)), Constants.popularDeckset.get(deckName));
                serverStatus.addGame(newGame);
                response.sendRedirect("/play/game/" + gameId + "/usr/" + creatorName);
            }
        } else {
            // unknown request
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
