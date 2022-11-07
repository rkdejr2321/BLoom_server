package gachon.BLoom.diagnose.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HashDto {

    private Long memberId;
    private String snsId;
    private String sns;
    private int hashCode;

}
