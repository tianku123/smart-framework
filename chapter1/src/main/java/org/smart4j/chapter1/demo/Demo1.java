package org.smart4j.chapter1.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * 九宫格问题
 */
public class Demo1 {

  public static void main(String[] args) {
    int[] arr = jiu(1, 3, 3);
  }

  /**
   * 九宫格问题
   * @param inputNum   九宫格范围内数字
   * @param length  连结数字次数
   * @param i2 多少宫格
   * @return
   */
  private static int[] jiu(int inputNum, int length, int i2) {
    int total = i2 * i2;
    if (inputNum > total) {
      throw new RuntimeException("超过数字范围");
    }
    /**
     * 获取数字对应在宫格内的坐标，坐标原点在左上角，从 1,1 坐标开始
     */
    Map<Integer, Integer[]> NumGetXy = new HashMap<>();
    Map<String, Integer> xyGetNum = new HashMap<>();
    for (int i = 1; i <= total; i++) {
      // x轴
      int x = i % i2 == 0 ? i2 : i % 2;
      int y = i % i2 == 0 ? i / i2 : 8 / i2 + 1;
      NumGetXy.put(i, new Integer[]{x, y});
      xyGetNum.put(x + "," + y, i);
    }
    /**
     * 获取 inputNum 的坐标
     */
    Integer[] inputXy = NumGetXy.get(inputNum);

    /**
     * 判断 坐标值是否为边界，有多少种可能性
     */
    int inputX = inputXy[0];
    int inputY = inputXy[1];
    Map<Integer, Integer[]> selected = calc(inputNum, inputX, inputY, xyGetNum, NumGetXy, length);
    for (Map.Entry<Integer, Integer[]> entry : selected.entrySet()) {
      System.out.println(entry.getKey());
    }
    return null;
  }

  /**
   * 获取所有相邻点
   * @param inputNum
   * @param inputX
   * @param inputY
   * @param xyGetNum
   * @param numGetXy
   * @param length
   * @return
   */
  private static Map<Integer, Integer[]> calc(int inputNum, int inputX, int inputY, Map<String, Integer> xyGetNum,
      Map<Integer, Integer[]> numGetXy, int length) {
    Map<Integer, Integer[]> selectedXy = new HashMap<>();
    selectedXy.put(inputNum, new Integer[]{inputX, inputY});
    if (selectedXy.size() == length + 1) {
      return selectedXy;
    }
    Map<Integer, Integer[]> abledXy = new HashMap<>();// 相邻的值
    for (Map.Entry<Integer, Integer[]> entry : numGetXy.entrySet()) {
      int num = entry.getKey();
      if (selectedXy.containsKey(num)) { // 排除已存在的
        continue;
      }
      Integer[] xy = entry.getValue();
      int x = xy[0];
      int y = xy[1];
      if (Math.abs(x - inputX) == 1 && Math.abs(y - inputY) == 0) {
        abledXy.put(num, xy);
      }
      if (Math.abs(x - inputX) == 0 && Math.abs(y - inputY) == 1) {
        abledXy.put(num, xy);
      }
      if (Math.abs(x - inputX) == 1 && Math.abs(y - inputY) == 1) {
        abledXy.put(num, xy);
      }
      // 如果没有得到相邻的值则失败，
      if (abledXy.size() == 0) {
        break;
      }
    }
    /**
     * 循环所有可能性
     */
    for (Map.Entry<Integer, Integer[]> entry : abledXy.entrySet()) {
      int num = entry.getKey();
      Integer[] xy = entry.getValue();
      int x = xy[0];
      int y = xy[1];

      calc(num, x, y, xyGetNum, numGetXy, --length);
    }
    return selectedXy;
  }

}
