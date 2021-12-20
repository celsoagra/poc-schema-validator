package org.example;

import com.google.common.base.Strings;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import java.util.Iterator;

public class AppTest {

    @Test(expected = ValidationException.class)
    public void givenInvalidInput_whenValidating_thenInvalid() throws ValidationException {
        JSONObject jsonSchema = new JSONObject(
                new JSONTokener(AppTest.class.getResourceAsStream("/schema.json")));
        JSONObject jsonSubject = new JSONObject(
                new JSONTokener(AppTest.class.getResourceAsStream("/request_invalid.json")));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
    }

    @Test
    public void givenValidInput_whenValidating_thenValid() throws ValidationException {
        JSONObject jsonSchema = new JSONObject(
                new JSONTokener(AppTest.class.getResourceAsStream("/schema.json")));
        JSONObject jsonSubject = new JSONObject(
                new JSONTokener(AppTest.class.getResourceAsStream("/request.json")));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);

        JSONObject properties = jsonSchema.getJSONObject("properties");
        Iterator iterator = properties.keys();

        String result = "";

        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            JSONObject property = properties.getJSONObject(key);

            if (property.get("type").toString().equals("string")) {
                String item = jsonSubject.getString(key);
                result += Strings.padStart(item, 20, ' ');
            } else if (property.get("type").toString().equals("integer") || property.get("type").toString().equals("number")) {
                Integer item = Integer.valueOf(jsonSubject.getInt(key));
                result += Strings.padStart(item.toString(), 20, '0');
            }

        }

        System.out.println(result);
    }
}
