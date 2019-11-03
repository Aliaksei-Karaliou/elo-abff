package com.github.aliakseikaraliou.eloabff.data

import org.jsoup.Jsoup

class TeamsByParser(
        val url: String
) : Parser {
    override fun parse(): List<Match> {
        return Jsoup
                .connect(url)
                .get()
                .select("table.news > tbody > tr.news")
                .mapIndexedNotNull { index, element ->
                    try {
                        val trs = element.select("td")
                        val date = trs[0].text()
                        val team1 = trs[1].select("span.hide-on-mobile").text().trim()
                        val score = try {
                            trs[3].select("b").text().split(":").map { it.toInt() }
                        } catch (e: Exception) {
                            trs[3].select("span.tournament").text()
                                    .replace("(", "")
                                    .replace(")", "")
                                    .split(":")
                                    .map { it.toInt() }
                        }
                        val team2 = trs[5].select("span.hide-on-mobile").text().trim()
                        Match(date, team1, score[0], team2, score[1])
                    }catch (e:Exception){
                        null
                    }
                }
    }

}