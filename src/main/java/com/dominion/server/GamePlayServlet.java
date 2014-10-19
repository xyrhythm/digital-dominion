package com.dominion.server;

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

public class GamePlayServlet extends DefaultServlet {

    private static final long serialVersionUID = 7620317194091811276L;
    private final ServerStatus serverStatus;
    private final static Pattern GAME_JOIN = Pattern.compile("/game/[0-9]*");
    private final static Pattern GAME_PLAY = Pattern.compile("/game/[0-9]*/usr/[a-zA-Z0-9]*");
    private final static String REDIRECT_HOME_PATH = "htmls/redirectHome.html";
    private final static String REDIRECT_HOME = IOUtils.readFile(REDIRECT_HOME_PATH, StandardCharsets.UTF_8);
    private final static String REDIRECT_GAME_PATH = "htmls/redirectGame.html";
    private final static String REDIRECT_GAME = IOUtils.readFile(REDIRECT_GAME_PATH, StandardCharsets.UTF_8);
    private final static String GAME_JOIN_FORM_PATH = "htmls/gameJoinForm.html";
    private final static String GAME_JOIN_FORM = IOUtils.readFile(GAME_JOIN_FORM_PATH, StandardCharsets.UTF_8);
    private final static String GAME_PLAY_PATH = "htmls/gamePlay.html";
    private final static String GAME_PLAY_PAGE = IOUtils.readFile(GAME_PLAY_PATH, StandardCharsets.UTF_8);

    public GamePlayServlet(ServerStatus serverStatus) {
        super();
        this.serverStatus = serverStatus;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();
        if (StringUtils.isEmpty(pathInfo)) {
            response.sendRedirect("view");
        } else if (GAME_JOIN.matcher(pathInfo).matches()) {
            final int gameId = Integer.parseInt(pathInfo.substring(6));
            final Game game = serverStatus.getGame(gameId);
            if (game != null && !game.isStarted()) {
                response.setContentType("text/html");
                response.getWriter().print(GAME_JOIN_FORM);
            } else {
                response.setContentType("text/html");
                response.getWriter().print(REDIRECT_HOME);
            }
        } else if (GAME_PLAY.matcher(pathInfo).matches()) {
            String[] params = pathInfo.split("/");
            final int gameId = Integer.parseInt(params[2]);
            final String usrName = params[4];
            final Game game = serverStatus.getGame(gameId);
            if (game != null) {
                if (game.containsPlayer(usrName)) {
                    response.setContentType("text/html");
                    response.getWriter().print(GAME_PLAY_PAGE);
                } else {
                    response.setContentType("text/html");
                    response.getWriter().print(REDIRECT_GAME);
                }
            } else {
                response.setContentType("text/html");
                response.getWriter().print(REDIRECT_HOME);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();
        if (StringUtils.isEmpty(pathInfo)) {
            response.sendRedirect("view");
        } else if (GAME_JOIN.matcher(pathInfo).matches()) {
            final int gameId = Integer.parseInt(pathInfo.substring(6));
            final Game game = serverStatus.getGame(gameId);
            final String userName = request.getParameter("user");
            if (game != null) {
                game.addPlayer(new Player(new PlayerInfo(userName)));
                response.sendRedirect("/play/game/" + gameId + "/usr/" + userName);
            } else {
                response.setContentType("text/html");
                response.getWriter().print(REDIRECT_HOME);
            }

        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
