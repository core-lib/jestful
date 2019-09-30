package org.qfox.elasticsearch.document;

import org.junit.Test;
import org.qfox.elasticsearch.BasicAPITest;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.math.BigDecimal;

/**
 * 文档API测试类
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 14:32
 */
public class DocumentAPITest extends BasicAPITest {

    private ProductAPI productAPI;

    @Test
    public void testIndex() {
        productAPI.index("basic", "product", "3", new Product("iPhone XR", new BigDecimal(5099)))
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Action1<DocumentIndexResult>() {
                    @Override
                    public void call(DocumentIndexResult documentIndexResults) {
                        System.out.println(documentIndexResults);
                    }
                });
    }

    @Test
    public void testCreate() {
        productAPI.create("basic", "product", new Product("iPhone XR", new BigDecimal(5099)))
                .doOnNext(new Action1<DocumentIndexResult>() {
                    @Override
                    public void call(DocumentIndexResult documentIndexResult) {
                        print(documentIndexResult);
                    }
                })
                .subscribe();
    }

    @Test
    public void testExists() {
        productAPI.exists("basic", "product", "4")
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean existed) {
                        print(existed);
                    }
                });
    }

    @Test
    public void testDelete() {
        productAPI.delete("basic", "product", "3")
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Action1<DocumentDeleteResult>() {
                    @Override
                    public void call(DocumentDeleteResult result) {
                        print(result);
                    }
                });
    }

    @Test
    public void testQuery() {
        productAPI.query("basic", "product", "3")
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Action1<DocumentQueryResult<Product>>() {
                    @Override
                    public void call(DocumentQueryResult<Product> result) {
                        print(result);
                    }
                });
    }

    @Test
    public void testQueryWithSource() {
        productAPI.query("basic", "product", "3", "name,price")
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Action1<DocumentQueryResult<Product>>() {
                    @Override
                    public void call(DocumentQueryResult<Product> result) {
                        print(result);
                    }
                });
    }

    @Test
    public void testSource() {
        productAPI.source("basic", "product", "3")
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Action1<Product>() {
                    @Override
                    public void call(Product result) {
                        print(result);
                    }
                });
    }

    @Test
    public void testSourceWithSource() {
        productAPI.source("basic", "product", "3", "name")
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Action1<Product>() {
                    @Override
                    public void call(Product result) {
                        print(result);
                    }
                });
    }
}
