/**
 *
 * File Name: cn.easybuild.core.hibernate.HqlUtils.java
 * Encoding UTF-8
 * Version: 1.0
 * Date: 2007-7-10
 * History:	
 */

package com.caul.core.jdbc;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * @author Jackson Fu (ant_bug@hotmail.com)
 * @version Revision: 1.00 Date: 2007-7-10
 */
public class SQLUtils {

    private static Logger logger = LoggerFactory.getLogger(cn.easybuild.core.jdbc.SQLUtils.class);

    /*-------------------- construtors --------------------*/

    private SQLUtils() {

    }
    
    /*----------------- public methods --------------------*/

    /**
     * 截取用于计算记录集总数的HQL语句的起始保留字“from”
     */
    private static final String START_RESERVE_WORDS = "from";

    /**
     * 截取用于计算记录集总数的HQL语句的起始保留字数组。
     * 由于这些保留字在HQL语句中均为可选，因此要依次判断HQL语句中是否存在这些保留字。如果发现存在
     * 这些保留字中的任何一个（依次），便确定用于计算记录集总数的HQL语句的结束位置；如果未不存在
     * 这些保留字，则结束位置为原HQL的长度。
     */
    private static final String[] END_RESERVE_WORDS = {" group ", " having ", " order "};

    private static final String[] REMOVED_WORDS = {" fetch"};

    private static final String COUNT_WORD = " count(*) ";

    /**
     * 计算记录集总数的HQL语法前缀
     */
    private static final String SELECT_COUNT_PREFIX = "select count(*) ";

    /**
     * 通过语法分析并根据传入的HQL语句生成计算记录集总数的HQL语句。
     *
     * @param hql 正常的HQL语句
     * @return
     */
    public static String generateCountStr(String hql) {

        for (String key : REMOVED_WORDS) {
            hql = Pattern.compile(key, Pattern.CASE_INSENSITIVE).matcher(hql).replaceAll("");
        }

        String lowerCaseHql = hql.toLowerCase();

        int start = lowerCaseHql.indexOf(START_RESERVE_WORDS);
        int end = hql.length();

        //判断是否包含保留字
        for (String key : END_RESERVE_WORDS) {
            if (lowerCaseHql.indexOf(key) > -1) {
                end = lowerCaseHql.indexOf(key);
                break;
            }
        }

        return SELECT_COUNT_PREFIX + hql.substring(start, end);
    }

    /**
     * 通过jsqlparser解析器传入的sql，生成统计总数的sql
     *
     * @param sql
     * @return
     */
    public static String generateCountSqlByParser(String sql) {
        try {
            Statement stmt = CCJSqlParserUtil.parse(sql);
            Select select = (Select) stmt;
            SelectBody selectBody = select.getSelectBody();
            if (selectBody instanceof PlainSelect) {
                //查询项改为总数，去除排序提升性能
                PlainSelect plainSelect = (PlainSelect) selectBody;
                SelectExpressionItem selectExpressionItem = new SelectExpressionItem(CCJSqlParserUtil.parseExpression(COUNT_WORD));
                plainSelect.setSelectItems(null);
                plainSelect.addSelectItems(selectExpressionItem);
                plainSelect.setOrderByElements(null);
            }
            return select.toString();
        } catch (Throwable e) {
            logger.error("不能解析的sql: " + sql);
            return SELECT_COUNT_PREFIX + " from (" + sql + ") countsql";
        }
    }
}
