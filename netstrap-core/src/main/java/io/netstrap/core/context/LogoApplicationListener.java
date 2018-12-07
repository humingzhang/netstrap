package io.netstrap.core.context;

import io.netstrap.core.context.event.StartedApplicationEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Logo打印
 *
 * @author minghu.zhang
 * @date 2018/11/29 14:37
 */
@Log4j2
public class LogoApplicationListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof StartedApplicationEvent) {
            printLogo();
        }

    }

    /**
     * 打印Logo
     */
    private void printLogo() {
        InputStream logoStream = LogoApplicationListener.class.getResourceAsStream("/logo.png");
        try {
            StringBuffer logo = new StringBuffer();
            logo.append("The server started successfully.\n\n");
            BufferedImage bi = ImageIO.read(logoStream);
            /**
             * 获取图像的宽度和高度
             */
            int width = bi.getWidth();
            int height = bi.getHeight();

            /**
             * 扫描图片
             */
            for (int i = 0; i < height; i++) {
                // 行扫描
                for (int j = 0; j < width; j++) {
                    int dip = bi.getRGB(j, i);
                    if (dip <= 0) {
                        logo.append("*");
                    } else {
                        logo.append(" ");
                    }
                }
                // 换行
                logo.append("\n");
            }

            log.info(logo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (logoStream != null) {
                try {
                    logoStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
