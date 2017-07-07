import java.util.Date;
import java.util.Set;

/**
 * Created by Microsoft on 07/07/2017.
 */
public class Advertisement {
    public int id;
    public int calculatedScore;
    public String title;
    public String submissionDate;
    public int price;
    public int viewCount;
    public String description;
    public Set<Image> images;
    public Set<Comment> comments;
    public LeafCategory category;
    public User user;
    public Location location;
    
}
