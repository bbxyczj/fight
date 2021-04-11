package fight.sort;

/*
 * ϣ��������ȡһ��С��n������d1��Ϊ��һ��������
 * ���ļ���ȫ����¼�ֳɣ�n����d1�����顣���о���Ϊd1�ı����ļ�¼����ͬһ�����С�
 * ���ڸ����ڽ���ֱ�Ӳ�������Ȼ��ȡ�ڶ�������d2<d1�ظ������ķ��������
 * ֱ����ȡ������dt=1(dt<dt-l<��<d2<d1)�������м�¼����ͬһ���н���ֱ�Ӳ�������Ϊֹ�� 
 */
public class ShellSort {
	public static void sort(int[] data) {
		for (int i = data.length / 2; i > 2; i /= 2) {
			for (int j = 0; j < i; j++) {
				insertSort(data, j, i);
			}
		}
		insertSort(data, 0, 1);
	}

	/**
	 * @param data
	 * @param j
	 * @param i
	 */
	private static void insertSort(int[] data, int start, int inc) {
		for (int i = start + inc; i < data.length; i += inc) {
			for (int j = i; (j >= inc) && (data[j] < data[j - inc]); j -= inc) {
				SortTest.swap(data, j, j - inc);
			}
		}
	}
}
/*
 * ���ڲ���������,�ǽ����������зָ������С�������зֱ���в������� ���� 
 * ������̣���ȡһ��������d1<n��������������d1������Ԫ�ط�һ�飬
 * ���ڽ���ֱ�Ӳ�������Ȼ��ȡd2<d1���ظ�������������������ֱ��di=1�� �����м�¼�Ž�һ����������Ϊֹ ���� 
 * ��ʼ��d=5 ����49 38 65 97 76 13 27 49 55 04 ���� 
 * 49 13 ����|-------------------| ���� 
 * 38 27     |-------------------| ���� 
 * 65 49 ����|-------------------| ���� 
 * 97 55     |-------------------| ���� 
 * 76 04 ����|-------------------| ���� 
 * һ�˽�� ����13 27 49 55 04 49 38 65 97 76 ���� 
 * d=3 ���� 13 27 49  55 04 49 38 65 97 76 ���� 
 * 13 55 38 76 |------------|------------|------------| ���� 
 * 27 04 65 |------------|------------| ���� 
 * 49 49 97 |------------|------------| ����
 * ���˽��  13 04 49* 38 27 49 55 65 97 76 ���� 
 * d=1 ����13 04 49 38 27 49 55 65 97 76
 * ���� |----|----|----|----|----|----|----|----|----| ���� ���˽�� ����
 * 04 13 27 38 49 49 55 65 76 97
 */

