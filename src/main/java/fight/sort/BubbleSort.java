package fight.sort;

/*
 * ð��������������ǣ�
 * ���αȽ����ڵ�����������С������ǰ�棬�������ں��档
 * ���ڵ�һ�ˣ����ȱȽϵ�1���͵�2��������С����ǰ�������ź�
 * Ȼ��Ƚϵ�2�����͵�3��������С����ǰ�������ź���˼�����
 * ֱ���Ƚ��������������С����ǰ�������ź����˵�һ�˽�����
 * ���������ŵ�������ڵڶ��ˣ��Դӵ�һ������ʼ�Ƚ�
 * ����Ϊ�������ڵ�2�����͵�3�����Ľ�����ʹ�õ�1��������С�ڵ�2��������
 * ��С����ǰ�������ź�һֱ�Ƚϵ������ڶ�������������һ��λ�����Ѿ������ģ���
 * �ڶ��˽������ڵ����ڶ���λ���ϵõ�һ���µ������
 * ����ʵ�������������ǵڶ���������������ȥ���ظ����Ϲ��̣�ֱ������������� 
 */
public class BubbleSort {
	public static void sort(int[] data) {
		for (int i = 0; i < data.length - 1; i++) {
			for (int j = 0; j < data.length - 1 - i; j++) {
				if (data[j] > data[j + 1]) {
					SortTest.swap(data, j, j + 1);
				}
			}
		}
	}
}
