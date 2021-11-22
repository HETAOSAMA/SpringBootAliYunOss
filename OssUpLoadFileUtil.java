
import com.aliyun.oss.OSS;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Component
@Data
@Slf4j
public class OssUpLoadFileUtil {
    @Autowired
    private OSS ossClient;
    @Autowired
    private AliYunOssConfig aliYunOssConfig;

    private List<String> list=null;
    public boolean uploadImages(MultipartFile[] images) {
        list = new LinkedList<>();
        // 校验图片格式
        boolean isLegal = false;
        for (MultipartFile multipartFile : images) {
            for (String type : aliYunOssConfig.getIMAGE_TYPE()) {
                if (StringUtils.endsWithIgnoreCase(multipartFile.getOriginalFilename(), type)) {
                    isLegal = true;
                    break;
                }
            }
        }

        if (!isLegal) {
            String message = "图片格式非.bmp.jpg.jpeg.gif.png格式";
            log.error(message);
            return false;
        }

        try {

            //上传图片
            for (MultipartFile multipartFile : images) {
                //获取文件上传流
                InputStream inputStream = multipartFile.getInputStream();
                //构建日期目录
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String datePath = dateFormat.format(new Date());
                //获取文件名称
                String originalFilename = multipartFile.getOriginalFilename();
                //重命名文件名
                String fileName = UUID.randomUUID().toString();
                //文件类型
                String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
                //文件名拼合文件后缀
                String newFile = fileName + fileType;
                //完整的路径
                String fileUrl = datePath + "/" + newFile;
                //上传文件
                ossClient.putObject(aliYunOssConfig.getBucketName(), fileUrl, inputStream);
                String url = "https://"+aliYunOssConfig.getBucketName()+"."+aliYunOssConfig.getEndpoint()+"/"+fileUrl+aliYunOssConfig.getIMAGE_STYLE();
                list.add(url);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return true;
    }

    public List<String> getFileUrl(){
        return this.list;
    }

}
