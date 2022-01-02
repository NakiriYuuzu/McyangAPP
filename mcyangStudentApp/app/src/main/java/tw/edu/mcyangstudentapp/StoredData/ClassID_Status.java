package tw.edu.mcyangstudentapp.StoredData;

import java.util.HashMap;
import java.util.Map;

public class ClassID_Status {
    private static final String classID_0001 = "數學";
    private static final String classID_0002 = "英文";

    Map<String, String> selections = new HashMap<>();

    public ClassID_Status() {
        initLocationData();
    }

    private void initLocationData() {
        selections.put("11365", classID_0001);
        selections.put("1221", classID_0002);
    }

    public String getClassNames(String major) {
        String classNames;
        if (selections == null || selections.size() == 0)
            return "查無此課程名稱";

        classNames = selections.get(major);
        if (classNames == null || classNames.equals(""))
            return "查無此課程名稱";

        return classNames;
    }
}
