package me.jsinco.withdrawals.hooks

import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI

object PlayerPointsHook {
    val playerPointsAPI: PlayerPointsAPI = PlayerPoints.getInstance().api
}