package gachon.BLoom.diagnose.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FeedDto {

    private List<String> user_sns;
    private String snsName;
}
