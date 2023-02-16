package com.lsh;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @Author: LiuShihao
 * @Date: 2023/2/2 20:40
 * @Desc:
 */
@ExtendWith(MockitoExtension.class)
public class Test0 {

    @Mock
    private List<String> list;

    /**
     * 验证行为
     * 一旦mock对象被创建了，mock对象会记住所有的交互。
     * 然后你就可能选择性的验证你感兴趣的交互，也就是说，你对这个Mock对象做的操作，都被记录下来了，验证的时候会是合法的。
     */
    @Test
    void testBehavior() {

        //构建moock数据
        List<String> list = mock(List.class);
        list.add("1");
        list.add("2");

        System.out.println(list.get(0)); // 会得到null ，前面只是在记录行为而已，没有往list中添加数据

        Mockito.verify(list).add("1"); // 正确，因为该行为被记住了
//        Mockito.verify(list).add("3");//报错，因为前面没有记录这个行为

    }


    /**
     * 存根用于预先说明当执行了什么操作的时候，产生一个什么响应
     * 主要用到when 、thenReturn、then、thenThrow、thenAnswer这几个函数。
     */
    @Test
    void testStub() {
        List<Integer> list = mock(ArrayList.class);

        when(list.get(0)).thenReturn(10);//当调用list.get(0) 这个方法的时候返回 10
        when(list.get(1)).thenReturn(20);
        when(list.get(2)).thenThrow(new RuntimeException("no such element"));//当调用list.get(0) 这个方法的时候抛出一个异常

        assertEquals(list.get(0), 10);
        assertEquals(list.get(1), 20);
        assertNull(list.get(4));
        assertThrows(RuntimeException.class, () -> {
            int x = list.get(2);
        });

        System.out.println("pass");
    }

    @Test
    public void testMock() {
        // 处理测试
        list = mock(ArrayList.class);
        // 设置当调用 list.get(0) 的时候，返回 “first value”
        when(list.get(0)).thenReturn("first value");
        assertThat(list.get(0), equalTo("first value"));
    }

    @Test
    public void testMockReturnMultiple() {
        Mockito.when(list.size()).thenReturn(1, 2, 3, 4);

        assertThat(list.size(), equalTo(1));
        assertThat(list.size(), equalTo(2));
        assertThat(list.size(), equalTo(3));
        assertThat(list.size(), equalTo(4));
    }


}
