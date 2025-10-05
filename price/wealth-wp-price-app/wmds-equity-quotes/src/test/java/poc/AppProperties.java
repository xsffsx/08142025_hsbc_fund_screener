package poc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="app")
@Getter
@Setter
public class AppProperties {

    private String test;

/*    public String getTest() {
        return this.test;
    }

    public void setTest(String test) {
        this.test = test;
    }*/
}
