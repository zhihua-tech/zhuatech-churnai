/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.churnai.workbench;

import static cn.zhuatech.churnai.workbench.WorkbenchInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);
    keys(r, "cohorts");
    Set<String> ids = new HashSet<>();
    List<Map<String, Object>> out = new ArrayList<>();
    for (var c : rows(r, "cohorts", 1, 100)) {
      keys(c, "id", "initial", "active");
      String id = id(c, "id");
      unique(ids, id);
      int initial = integer(c, "initial", 1, 1000000000);
      var active = list(c, "active", 1, 36);
      List<Map<String, Object>> periods = new ArrayList<>();
      for (int i = 0; i < active.size(); i++) {
        int n = integer(Collections.singletonMap("n", active.get(i)), "n", 0, initial);
        periods.add(
            Map.of(
                "period",
                i + 1,
                "active",
                n,
                "retentionPercent",
                BigDecimal.valueOf(n)
                    .multiply(new BigDecimal("100"))
                    .divide(BigDecimal.valueOf(initial), 6, RoundingMode.HALF_UP)
                    .stripTrailingZeros()));
      }
      out.add(Map.of("id", id, "initial", initial, "periods", periods));
    }
    return Map.of(
        "cohorts", out, "method", "ACTIVE_DIVIDED_BY_INITIAL", "individualPrediction", false);
  }
}
