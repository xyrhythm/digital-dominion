package com.dominion.server;

import com.dominion.common.Constants;
import com.dominion.common.Game;
import com.dominion.common.playerAction.PlayerAction;
import com.dominion.utils.JsonUtils;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlet.DefaultServlet;

public class GameEventServlet extends DefaultServlet {

    private static final long serialVersionUID = 3338347278442096601L;
    private final static Pattern GAME_USR = Pattern.compile("/game/[0-9]*/usr/[a-zA-Z0-9]*");

    private final ServerStatus serverStatus;

    public GameEventServlet(ServerStatus serverStatus) {
        super();
        this.serverStatus = serverStatus;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();

        if (GAME_USR.matcher(pathInfo).matches()) {
            String[] params = pathInfo.split("/");
            final int gameId = Integer.parseInt(params[2]);
            final String usrName = params[4];
            final Game game = serverStatus.getGame(gameId);
            PlayerAction playerAction = game.getPlayerAction();
            if (playerAction != null) {
                response.setContentType(Constants.JSON_TYPE);
                response.getWriter().print(JsonUtils.writeJsonObjectToString(playerAction));
            } else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
    }

}
