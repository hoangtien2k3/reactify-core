package io.hoangtien2k3.commons.model.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogRequest {
    private boolean enable = true;
}
