package server;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomLocalDateTimeSerializer 
extends StdSerializer<LocalDate> {

  private static DateTimeFormatter formatter = 
    DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public CustomLocalDateTimeSerializer() {
      this(null);
  }

  public CustomLocalDateTimeSerializer(Class<LocalDate> t) {
      super(t);
  }
  
  @Override
  public void serialize(
    LocalDate value,
    JsonGenerator gen,
    SerializerProvider arg2)
    throws IOException, JsonProcessingException {

      gen.writeString(formatter.format(value));
  }
}
