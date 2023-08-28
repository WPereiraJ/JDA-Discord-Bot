package me.DiscordBot.main.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Commands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ritual")) {
            event.deferReply().queue();
            String ritualNameOption = event.getOption("nome").getAsString().toLowerCase();

            try {
                String ritualInfo = RitualInfoProvider.getRitualInfo(ritualNameOption);

                if (ritualInfo != null) {
                    JSONObject ritualObject = new JSONObject(ritualInfo);

                    String ritualElement = ritualObject.getString("elemento");
                    String ritualDescription = ritualObject.getString("descricao");
                    String ritualExecution = ritualObject.optString("execucao", "");
                    String ritualRange = ritualObject.optString("alcance", "");
                    String discentePart = ritualObject.optString("discente", "");
                    String verdadeiroPart = ritualObject.optString("verdadeiro", "");
                    String ritualDetails = ritualObject.optString("detalhes", "");

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.BLACK);
                    embed.setThumbnail("https://static.wikia.nocookie.net/ordemparanormal/images/2/24/S%C3%ADmbolo_Maior_ocultista.png/revision/latest/scale-to-width-down/1000?cb=20211126110518&path-prefix=pt-br");
                    embed.setTitle("**" + ritualNameOption.toUpperCase() + "**");
                    embed.addField("Elemento", ritualElement, false);
                    embed.addField("Descrição do Ritual", ritualDescription, false);
                    embed.addField("Execução", ritualExecution, false);
                    embed.addField("Alcance", ritualRange, false);

                    if (!discentePart.isEmpty()) {
                        embed.addField("Discente", discentePart, false);
                    }

                    if (!verdadeiroPart.isEmpty()) {
                        embed.addField("Verdadeiro", verdadeiroPart, false);
                    }

                    if (!ritualDetails.isEmpty()) {
                        embed.addField("Detalhes", ritualDetails, false);
                    }

                    embed.setFooter("Feito por @SirYuky");

                    event.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();
                } else {
                    event.getHook().sendMessage("Ritual " + ritualNameOption + " não encontrado").setEphemeral(false).queue();
                }
            } catch (IOException e) {
                event.getHook().sendMessage("Erro ao ler o arquivo de rituais.").setEphemeral(true).queue();
                e.printStackTrace();
            }
        }
    }

    private String getRitualElement(String elementLine) {
        String element = elementLine.trim().toUpperCase();
        if (!element.matches("SANGUE|CONHECIMENTO|MEDO|MORTE|ENERGIA")) {
            return "ELEMENTO ESCOLHIDO";
        }
        return element;
    }

    private String getPartValue(String[] parts, String label) {
        for (String part : parts) {
            if (part.startsWith(label)) {
                return part.substring(label.length()).trim();
            }
        }
        return null;
    }

    public static class RitualInfoProvider {

        public static String getRitualInfo(String ritualName) throws IOException {
            InputStream inputStream = RitualInfoProvider.class.getResourceAsStream("/me/DiscordBot/main/resources/rituais.json");

            if (inputStream != null) {
                String content = TextFileReader.readTextFile(inputStream);
                JSONArray jsonArray = new JSONArray(content);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject ritualObject = jsonArray.getJSONObject(i);
                    if (ritualObject.getString("nome").equalsIgnoreCase(ritualName)) {
                        return ritualObject.toString();
                    }
                }
            }

            return null;
        }
    }

    public static class TextFileReader {

        public static String readTextFile(InputStream inputStream) throws IOException {
            StringBuilder content = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    content.append(line);
                    content.append("\n");
                }
            }

            return content.toString();
        }
    }
}
