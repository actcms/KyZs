package three.com.materialdesignexample.Models;

/**
 * Created by Administrator on 2015/10/21.
 */
public class Score {
    private String ScoreName;
    private String point=null;
    private String testScore=null;
    private String type=null;
    private String examScore=null;
    private String credit=null;
    private int Id;

    public String getExamScore() {
        return examScore;
    }

    public void setExamScore(String examScore) {
        this.examScore = examScore;
    }


    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getScoreName() {
        return ScoreName;
    }

    public void setScoreName(String scoreName) {
        ScoreName = scoreName;
    }

    public String getTestScore() {
        return testScore;
    }

    public void setTestScore(String testScore) {
        this.testScore = testScore;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
