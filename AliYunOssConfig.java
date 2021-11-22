
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aliyun")
@Data
public class AliYunOssConfig {
    private String accessKeyId;
    private String accessKeySecret;
    //存储桶所在地域的域名
    private String endpoint;
    //存储桶名称
    private String bucketName;

    //图片格式
    private final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};

    //视频格式
    private final String[] VIDEOS_TYPE = new String[]{".mp4"};

    //图片压缩样式，拼在返回路径后
    private final String IMAGE_STYLE = "?x-oss-process=style/xxxxxx";
    @Bean
    public OSS oSSClient() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }
}
