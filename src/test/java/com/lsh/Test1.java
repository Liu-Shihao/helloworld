package com.lsh;

import com.lsh.service.HelloService;
import com.lsh.utils.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @Author: LiuShihao
 * @Date: 2023/2/2 20:05
 * @Desc: Mockito 就是可以制作假替身，代替外部依赖，模拟我们自定义的返回操作的替身测试框架。
 *
 * spying 英文是间谍的意思，说明这不是替身，而是真正对象中一个特殊的对象
 *
 * Mock 是对被测试类的依赖对象构造替身操作，还需将替身注入到被测试类。
 * 注入可以使用 @InjectMocks 注解标识需要被注入的类。
 * 依赖的其他类 就用@Mock 注解标识
 * Mockito 自动将替身注入到被测试类。
 *
 * 通用参数匹配：
 * any():匹配任意对象，带着any名称的方法 还有anyInt(), anyObject() 都是一样的用法
 * eq()：指定特殊的具体value匹配
 * isA()：匹配任意某个类及其子类的对象
 *
 * 通过 Mock 构造出替身对象后，我们还需要设置替身对象的行为。有点类似录制与播放。比方说设置替身对象调用某方法的返回值。
 *
 * 设置某方法调用的返回值
 * 最常用的一种方式，就是设置某种方法根据某个入参的返回值是什么。或者设置抛出什么异常。
 *
 * 设置返回对象方式一 when().thenReturn();
 * 设置返回对象方式二 doReturn().when(对象).对象方法;
 * 设置抛出异常 when().thenThrow();
 */
@ExtendWith(MockitoExtension.class)
public class Test1 {

    ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    @Spy
    @InjectMocks
    private HelloService helloService;

    @Mock
    StringUtil stringUtil;

    /**
     *
     */
    @Test
    public void test(){
        String name = "sdjchaouweo";
        Mockito.when(helloService.getString(name)).thenReturn("success");




    }
}
