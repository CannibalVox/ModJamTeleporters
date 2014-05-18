package net.modyssey.teleporters.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.modyssey.teleporters.markets.stock.StockList;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

public class MarketDataParser {
    private Gson gson;

    public List<StockList> parseMarketData(InputStream stream) {
        if (stream == null) {
            throw new RuntimeException("Mod Data json file could not be found.");
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();

        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(stream, writer);
            String dataText = writer.toString();
            data = gson.fromJson(dataText, ModData.class);

            if (data == null) {
                throw proxy.createParseException("Mod Data json file parsed to a null value.");
            }
        } catch (IOException ex) {
            throw proxy.createParseException("Error loading Mod Data json file.", ex);
        } catch (JsonSyntaxException ex) {
            throw proxy.createParseException("Error parsing the Mod Data json file.", ex);
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {}
        }
    }
}
