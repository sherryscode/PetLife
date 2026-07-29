package com.petlife.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petlife.common.entity.GoodsOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsOrderMapper extends BaseMapper<GoodsOrder> {

    @Select("SELECT go.* FROM goods_order go " +
            "JOIN goods_order_item goi ON go.id = goi.order_id " +
            "JOIN goods g ON goi.goods_id = g.id " +
            "WHERE g.merchant_id = #{merchantId}")
    List<GoodsOrder> selectByMerchantId(@Param("merchantId") Long merchantId);

    @Select("SELECT go.* FROM goods_order go " +
            "JOIN goods_order_item goi ON go.id = goi.order_id " +
            "JOIN goods g ON goi.goods_id = g.id " +
            "WHERE g.merchant_id = #{merchantId} " +
            "AND DATE(go.created_at) = DATE(NOW())")
    List<GoodsOrder> selectTodayByMerchantId(@Param("merchantId") Long merchantId);

    @Select("SELECT DATE_FORMAT(go.created_at, '%Y-%m-%d') as order_date, " +
            "COUNT(DISTINCT go.id) as order_count, " +
            "SUM(go.total_amount) as total_amount " +
            "FROM goods_order go " +
            "JOIN goods_order_item goi ON go.id = goi.order_id " +
            "JOIN goods g ON goi.goods_id = g.id " +
            "WHERE g.merchant_id = #{merchantId} " +
            "GROUP BY DATE_FORMAT(go.created_at, '%Y-%m-%d') " +
            "ORDER BY order_date DESC LIMIT #{days}")
    List<Map<String, Object>> selectOrderTrendByMerchantId(@Param("merchantId") Long merchantId, @Param("days") Integer days);

    @Select("SELECT DATE_FORMAT(created_at, '%Y-%m-%d') as order_date, " +
            "COUNT(id) as order_count, " +
            "SUM(total_amount) as total_amount " +
            "FROM goods_order " +
            "GROUP BY DATE_FORMAT(created_at, '%Y-%m-%d') " +
            "ORDER BY order_date DESC LIMIT #{days}")
    List<Map<String, Object>> selectOrderTrend(@Param("days") Integer days);

    @Select("SELECT COUNT(*) FROM goods_order WHERE DATE(created_at) = DATE(NOW())")
    Long selectTodayOrderCount();

    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM goods_order WHERE DATE(created_at) = DATE(NOW())")
    BigDecimal selectTodayRevenue();
}
