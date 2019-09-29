package org.qfox.elasticsearch.document;

import org.junit.Test;
import org.qfox.elasticsearch.BasicAPITest;
import org.qfox.jestful.core.Status;
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

    private DocumentAPI documentAPI;

    @Test
    public void testIndex() {
        documentAPI.index("basic", "product", "3", new Product("iPhone XR", new BigDecimal(5099)))
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
        documentAPI.create("basic", "product", new Product("iPhone XR", new BigDecimal(5099)))
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
        documentAPI.exists("basic", "product", "3")
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Action1<Status>() {
                    @Override
                    public void call(Status status) {
                        System.out.println(status);
                    }
                });
    }
}
