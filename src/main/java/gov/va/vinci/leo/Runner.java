package gov.va.vinci.leo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class Runner {
    private final static Logger LOGGER = Logger.getLogger(Runner.class.getName());

    public static void main(String[] args) {
        if (args.length == 0) {
            LOGGER.warning("A configuration file location needs to be specified.");
        }
        new Runner(args[0]);
    }

    public Runner(String configFile) {
        File configF = new File(configFile);
        if (!configF.exists()) {
            LOGGER.warning("configuration file doesn't exist, please check: " + configF.getAbsolutePath());
            System.exit(0);
        }
        JSONParser parser = new JSONParser();
        try {
            JSONObject config = (JSONObject) parser.parse(new FileReader(configF));
            if (!config.containsKey("scripts")) {
                LOGGER.warning("scripts configuration needs start with 'scripts'.");
                return;
            }
            JSONArray scriptsConfig = (JSONArray) config.get("scripts");
            for (Object scriptConfig : scriptsConfig) {
                SingleScriptRunner srun = new SingleScriptRunner((JSONObject) scriptConfig);
                srun.executeScript();
            }
        } catch (FileNotFoundException e) {
            LOGGER.warning(e.getMessage());
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        } catch (ParseException e) {
            LOGGER.warning(e.getMessage());
        }
    }
}