package cs5500.cs5500.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class URLNotFoundException extends RuntimeException {

  public URLNotFoundException(String exception) {
    super(exception);
  }
}
