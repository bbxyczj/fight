package fight.sort;

import java.util.Arrays;
import java.util.Random;

public class SortTest {

	// ��Ͱ��ʼ����С
	private static final int MAXINITPERBUCKET = 100000;
	// Ͱ������
	private static final int MAXBUCKET = 1000;

	private static int resources[] = new int[MAXBUCKET *MAXINITPERBUCKET ];

	static {
		// ������ɴ����������Ԫ��
		Random random = new Random();
		int len = MAXINITPERBUCKET * MAXBUCKET;
		for(int index = 0 ; index < len; index++) {
			resources[index] = random.nextInt(len);
		}
	}

	public static void main(String[] args) {

		System.out.println("����ǰ��" + Arrays.toString(resources));
		 InsertSort.sort(resources);
		// BubbleSort.sort(arr);
		// SelectionSort.sort(arr);
		// ShellSort.sort(arr);
		// QuickSort.sort(arr);
		// MergeSort.sort(arr);
		// HeapSort.sort(arr);
		System.out.println("�����" + Arrays.toString(resources));
	}

	/*
	 * ���������е�����Ԫ��
	 */
	public static void swap(int[] data, int i, int j) {
		int temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}
}
