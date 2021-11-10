package com.psi.canal.listener;

import com.alibaba.fastjson.JSON;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.psi.content.feign.ContentFeign;
import com.psi.content.pojo.Content;
import com.psi.entity.Result;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * 监听广告的增删改，
 * 并根据增删改的数据使用feign查询对应分类的所有广告，
 * 将广告存入到Redis中
 */
@CanalEventListener
public class MyCanalEventListener {
    @Autowired
    private ContentFeign contentFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;//操作字符串

    /***
     * 监听MySQL增删改操作
     * @param eventType
     */
    @ListenPoint(destination = "example",
            schema = "dongyimaidb",
            table = {"tb_content_category", "tb_content"},
            eventType = {CanalEntry.EventType.UPDATE,
                    CanalEntry.EventType.DELETE,
                    CanalEntry.EventType.INSERT})
    public void whenDatabaseChanged(CanalEntry.EventType eventType,
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
}