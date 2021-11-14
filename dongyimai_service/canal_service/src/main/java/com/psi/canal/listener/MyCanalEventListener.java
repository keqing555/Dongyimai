package com.psi.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.psi.content.feign.ContentFeign;
import com.psi.content.pojo.Content;
import com.psi.entity.Result;
import com.psi.item.feign.PageFeign;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.File;
import java.util.List;

/***
 * 监听MySQL增删改操作
 */
@CanalEventListener
public class MyCanalEventListener {
    @Autowired
    private ContentFeign contentFeign;
    @Autowired
    private PageFeign pageFeign;

    //静态文件的生成路径
    @Value("${pagepath}")
    private String pagepath;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;//操作字符串


    /**
     * 监听广告的增删改，
     * 并根据增删改的数据使用feign查询对应分类的所有广告，
     * 将广告存入到Redis中
     */
    @ListenPoint(destination = "example",
            schema = "dongyimaidb",
            table = {"tb_content_category", "tb_content"},
            eventType = {CanalEntry.EventType.UPDATE,
                    CanalEntry.EventType.DELETE,
                    CanalEntry.EventType.INSERT})
    public void whenContentChanged(CanalEntry.EventType eventType,
                                   CanalEntry.RowData rowData) {
        //获取categoryId
        String categoryId = getCategoryId(eventType, rowData);
        //使用feign获取该分类下的所有广告集合
        Result<List<Content>> result = contentFeign.findByCategoryId(Long.parseLong(categoryId));
        List<Content> contentList = result.getData();
        //使用RedisTemplate存储到redis里
        stringRedisTemplate.boundValueOps("content_" + categoryId)
                .set(JSON.toJSONString(contentList));//转为json字符串
        System.out.println("redis数据已更新");
    }

    /***
     * 获取categoryId
     * @param eventType
     * @param rowData
     * @return
     */
    private String getCategoryId(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        String categoryId = "";
        List<CanalEntry.Column> columnList = null;
        //如果是删除，获取beforeList
        if (eventType == CanalEntry.EventType.DELETE) {
            columnList = rowData.getBeforeColumnsList();
        } else {
            //如果是添加或修改，获取afterList
            columnList = rowData.getAfterColumnsList();
        }
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            if (column.getName().equalsIgnoreCase("category_id")) {
                categoryId = column.getValue();
            }
        }
        return categoryId;
    }

    @ListenPoint(destination = "example",
            schema = "dongyimaidb",
            table = {"tb_goods"},
            eventType = {CanalEntry.EventType.UPDATE,
                    CanalEntry.EventType.INSERT,
                    CanalEntry.EventType.DELETE})
    public void whenGoodsChanged(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        //spuId
        String goodsId = "";
        //判断操作类型
        if (eventType == CanalEntry.EventType.DELETE) {
            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : beforeColumnsList) {
                if (column.getName().equals("id")) {
                    goodsId = column.getValue();
                    break;
                }
            }
            //删除静态页面
            //静态文件路径
            File file = new File(pagepath, goodsId + ".html");
            if (file.exists()) {
                file.delete();
                System.out.println("已删除静态文件：" + file.getName());
            } else {
                System.out.println("要删除的静态文件不存在");
            }
        } else {
            //修改了数据，或者添加了商品
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            for (CanalEntry.Column column : afterColumnsList) {
                if (column.getName().equals("id")) {
                    goodsId = column.getValue();
                    break;
                }
            }
            //重新生成静态页面
            //静态文件路径
            File file = new File(pagepath, goodsId + ".html");
            if (file.exists()) {
                pageFeign.createHtml(Long.parseLong(goodsId));
                System.out.println("静态页面已修改");
            } else {
                pageFeign.createHtml(Long.parseLong(goodsId));
                System.out.println("静态页面已创建");
            }
        }
    }
}