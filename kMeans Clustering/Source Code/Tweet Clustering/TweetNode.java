public class TweetNode {
	int clusterID; 
	float dist;
	String id;
	String text;
	String profile_image_url; 
	String from_user;
	String from_user_id; 
	String geo;
	String iso_language_code;
	String from_user_id_str; 
	String created_at;
	String source;
	String id_str;
	String from_user_name;
	String profile_image_url_https;
	String[] metadata;
	
	public TweetNode()
	{
		dist = Float.MAX_VALUE;
	}
}
