
package com.example.demo1.Code.recheck_demo;



import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.*;

public class StringCompute {

    /**
     * ���������ַ��������ƶ�
     *
     * @param strA �ַ���A
     * @param strB �ַ���B
     * @return ���ƶ�
     */
    public static double SimilarDegree(String strA, String strB) {

        String newStrA = removeSign(strA);
        String newStrB = removeSign(strB);

        //�ýϴ���ַ���������Ϊ��ĸ�������Ӵ���Ϊ���Ӽ�����ִ����ƶ�
        int temp = Math.max(newStrA.length(), newStrB.length());
        int temp2 = longestCommonSubstring(newStrA, newStrB).length();

        return temp2 * 1.0 / temp;
    }

    /**
     * ���ַ�����������������д��һ��
     *
     * @param str Ŀ���ַ���
     * @return ��������ַ���
     */
    public static String removeSign(String str) {

        StringBuilder stringBuilder = new StringBuilder();

        //����str,���α���str�еĺ��֡����֡���ĸ��sb
        for (char item : str.toCharArray())
            if (charReg(item)) {
                stringBuilder.append(item);
            }
        return stringBuilder.toString();
    }

    /**
     * �ж��ַ��Ƿ��Ǻ��֡����֡���ĸ(���Է��Ž������ƶȶԱ�)
     *
     * @param charValue ���ж��ַ�
     * @return �жϽ��
     */
    public static boolean charReg(char charValue) {
        return (charValue >= 0x4E00 && charValue <= 0X9FA5) || (charValue >= 'a' && charValue <= 'z')
                || (charValue >= 'A' && charValue <= 'Z') || (charValue >= '0' && charValue <= '9');
    }

    /**
     * ���ö�̬�滮�㷨�󹫹��Ӵ�����õ��Ӵ���һ��Ϊ�����Ӵ�
     *
     * @param strA ��һ���ַ���
     * @param strB �ڶ����ַ���
     * @return �����Ӵ�
     */
    public static String longestCommonSubstring(String strA, String strB) {

        char[] chars_strA = strA.toCharArray();
        char[] chars_strB = strB.toCharArray();
        int m = chars_strA.length;
        int n = chars_strB.length;

        /*
          ��ʼ����������,matrix[0][0]=0��
          ����ַ�����chars_strA��chars_strB�Ķ�Ӧλ��ͬ,��matrix[i][j]��ֵΪ���Ͻǵ�ֵ��1��
          ����matrix[i][j]��ֵ�������Ϸ��������λ�õĽϴ�ֵ,��������������ֵΪ0.
         */
        int[][] matrix = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (chars_strA[i - 1] == chars_strB[j - 1])
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                else
                    matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
            }
        }

        /*
         �����У����matrix[m][n]��ֵ������matrix[m-1][n]��ֵҲ������matrix[m][n-1]��ֵ��
         ��matrix[m][n]��Ӧ���ַ�Ϊ�����ַ�Ԫ�����������result�����С�
         */
        char[] result = new char[matrix[m][n]];
        int currentIndex = result.length - 1;
        while (matrix[m][n] != 0) {
            if (matrix[n] == matrix[n - 1])
                n--;
            else if (matrix[m][n] == matrix[m - 1][n])
                m--;
            else {
                result[currentIndex] = chars_strA[m - 1];
                currentIndex--;
                n--;
                m--;
            }
        }
        return new String(result);
    }

    /**
     * ���ظ���ת��Ϊ�ٷ���
     *
     * @param result �ظ���
     * @return �ظ��ʰٷֱ�
     */
    public static String similarityResult(double result) {
        return NumberFormat.getPercentInstance(new Locale("en ", "US ")).format(result);
    }

    /**
     * ���Ҷ���������ƶ� 1
     * @param strA �ַ���
     * @param strB �ַ���
     * @return ���ƶ�
     */
    public static double getSimilarity(String strA, String strB) {

        String doc1 = removeSign(strA);
        String doc2 = removeSign(strB);
        if (doc1.trim().length() > 0 && doc2.trim().length() > 0) {

            Map<Integer, int[]> AlgorithmMap = new HashMap<>();

            //�������ַ����е������ַ��Լ����ֵ�������װ����AlgorithmMap��
            for (int i = 0; i < doc1.length(); i++) {
                char d1 = doc1.charAt(i);
                if(isHanZi(d1)){//�������ֲ�����
                    int charIndex = getGB2312Id(d1);//�����ַ���Ӧ��GB2312����
                    if(charIndex != -1){
                        int[] fq = AlgorithmMap.get(charIndex);
                        if(fq != null && fq.length == 2){
                            fq[0]++;//���и��ַ�����1
                        }else {
                            fq = new int[2];
                            fq[0] = 1;
                            fq[1] = 0;
                            AlgorithmMap.put(charIndex, fq);//�����ַ���map
                        }
                    }
                }
            }

            for (int i = 0; i < doc2.length(); i++) {
                char d2 = doc2.charAt(i);
                if(isHanZi(d2)){
                    int charIndex = getGB2312Id(d2);
                    if(charIndex != -1){
                        int[] fq = AlgorithmMap.get(charIndex);
                        if(fq != null && fq.length == 2){
                            fq[1]++;
                        }else {
                            fq = new int[2];
                            fq[0] = 0;
                            fq[1] = 1;
                            AlgorithmMap.put(charIndex, fq);
                        }
                    }
                }
            }

            Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
            double sqdoc1 = 0;
            double sqdoc2 = 0;
            double denominator = 0;
            while(iterator.hasNext()){
                int[] c = AlgorithmMap.get(iterator.next());
                denominator += c[0]*c[1];
                sqdoc1 += c[0]*c[0];
                sqdoc2 += c[1]*c[1];
            }

            return denominator / Math.sqrt(sqdoc1*sqdoc2);//���Ҽ���
        } else {
            throw new NullPointerException(" the Document is null or have not cahrs!!");
        }
    }

    public static boolean isHanZi(char ch) {
        // �ж��Ƿ���
        return (ch >= 0x4E00 && ch <= 0x9FA5);
        /*if (ch >= 0x4E00 && ch <= 0x9FA5) {//����
            return true;
        }else{
            String str = "" + ch;
            boolean isNum = str.matches("[0-9]+");
            return isNum;
        }*/
        /*if(Character.isLetterOrDigit(ch)){
            String str = "" + ch;
            if (str.matches("[0-9a-zA-Z\\u4e00-\\u9fa5]+")){//������
                return true;
            }else return false;
        }else return false;*/
    }

    /**
     * ���������Unicode�ַ�����ȡ����GB2312�������ascii���룬
     *
     * @param ch �����GB2312�����ַ�����ASCII�ַ�(128��)
     * @return ch��GB2312�е�λ�ã�-1��ʾ���ַ�����ʶ
     */
    public static short getGB2312Id(char ch) {
        try {
            byte[] buffer = Character.toString(ch).getBytes("GB2312");
            if (buffer.length != 2) {
                // ���������bufferӦ���������ֽڣ�����˵��ch������GB2312���룬�ʷ���'?'����ʱ˵������ʶ���ַ�
                return -1;
            }
            int b0 = (int) (buffer[0] & 0x0FF) - 161; // �����A1��ʼ����˼�ȥ0xA1=161
            int b1 = (int) (buffer[1] & 0x0FF) - 161;
            return (short) (b0 * 94 + b1);// ��һ���ַ������һ���ַ�û�к��֣����ÿ����ֻ��16*6-2=94������
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return -1;
    }



    /**
     * ���Ҷ�������ظ��� 2
     */
    /*public static double computeTxtSimilar(String txtLeft, String txtRight) {


        //�����ĵ����ܴʿ�
        List<String> totalWordList = new ArrayList<>();
        //�����ĵ��Ĵ�Ƶ
        Map<String, Integer> leftWordCountMap = getWordCountMap(txtLeft, totalWordList);
        Map<String, Float> leftWordTfMap = calculateWordTf(leftWordCountMap);

        Map<String, Integer> rightWordCountMap = getWordCountMap(txtRight, totalWordList);
        Map<String, Float> rightWordTfMap = calculateWordTf(rightWordCountMap);

        //��ȡ�ĵ�������ֵ
        List<Float> leftFeature = getTxtFeature(totalWordList, leftWordTfMap);
        List<Float> rightFeature = getTxtFeature(totalWordList, rightWordTfMap);

        //�����ĵ���Ӧ����ֵ��ƽ���͵�ƽ����
        float leftVectorSqrt = calculateVectorSqrt(leftWordTfMap);
        float rightVectorSqrt = calculateVectorSqrt(rightWordTfMap);

        //�������Ҷ���ʽ���������ҹ�ʽ�еķ���
        float result = getCosValue(leftFeature, rightFeature);

        //�������Ҷ�����������ĵ�������ֵ
        double cosValue = 0;
        if (result > 0) {
            cosValue = result / (leftVectorSqrt * rightVectorSqrt);
        }
        cosValue = Double.parseDouble(String.format("%.4f", cosValue));
        return cosValue;

    }


    public static Map<String, Integer> getWordCountMap(String text, List<String> totalWordList) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        List<Term> words = HanLP.segment(text);
        int count;
        for (Term tm : words) {
            //ȡ����Ϊ�����ֻ��������������ʻ�������Ϊ�ؼ���
            if (tm.word.length() > 1 && (tm.nature == Nature.n || tm.nature == Nature.vn)) {
                count = 1;
                if (wordCountMap.containsKey(tm.word)) {
                    count = wordCountMap.get(tm.word) + 1;
                    wordCountMap.remove(tm.word);
                }
                wordCountMap.put(tm.word, count);
                if (!totalWordList.contains(tm.word)) {
                    totalWordList.add(tm.word);
                }
            }
        }
        return wordCountMap;
    }

    //����ؼ��ʴ�Ƶ
    private static Map<String, Float> calculateWordTf(Map<String, Integer> wordCountMap) {
        Map<String, Float> wordTfMap;
        int totalWordsCount = 0;
        Collection<Integer> cv = wordCountMap.values();
        for (Integer count : cv) {
            totalWordsCount += count;
        }

        wordTfMap = new HashMap<>();
        Set<String> keys = wordCountMap.keySet();
        for (String key : keys) {
            wordTfMap.put(key, wordCountMap.get(key) / (float) totalWordsCount);
        }
        return wordTfMap;
    }

    //�����ĵ���Ӧ����ֵ��ƽ���͵�ƽ����
    private static float calculateVectorSqrt(Map<String, Float> wordTfMap) {
        float result = 0;
        Collection<Float> cols = wordTfMap.values();
        for (Float temp : cols) {
            if (temp > 0) {
                result += temp * temp;
            }
        }
        return (float) Math.sqrt(result);
    }

    private static List<Float> getTxtFeature(List<String> totalWordList, Map<String, Float> wordCountMap) {
        List<Float> list = new ArrayList<>();
        for (String word : totalWordList) {
            float tf = 0;
            if (wordCountMap.containsKey(word)) {
                tf = wordCountMap.get(word);
            }
            list.add(tf);
        }
        return list;
    }

    private static float getCosValue(List<Float> leftFeature, List<Float> rightFeature) {
        float result = 0;
        float tempX;
        float tempY;
        for (int i = 0; i < leftFeature.size(); i++) {
            tempX = leftFeature.get(i);
            tempY = rightFeature.get(i);
            if (tempX > 0 && tempY > 0) {
                result += tempX * tempY;
            }
        }
        return result;
    }
    */


    /**
     *���Ҷ�������ظ��� 3
     */
   /* public static Double getCosineSimilarity(String textA,String textB){
        // ���ı�����ȡ���ؼ�������
        List<String> wordListA = extractWordFromText(textA);
        List<String> wordListB = extractWordFromText(textB);

        List<Double> vectorA = new ArrayList<>();
        List<Double> vectorB = new ArrayList<>();
        // ���ؼ�������ת��Ϊ�������������� vectorA �� vectorB ��
        convertWordList2Vector(wordListA,wordListB,vectorA,vectorB);

        // ���������нǵ�����ֵ
        double cosine = Double.parseDouble(String.format("%.4f",countCosine(vectorA,vectorB)));
        return cosine;
    }

    // ��ȡ�ı�����ʵ��Ĵ�
    private static List<String> extractWordFromText(String text){
        // resultList ���ڱ�����ȡ��Ľ��
        List<String> resultList = new ArrayList<>();

        // �� text Ϊ���ַ���ʱ��ʹ�÷ִʺ����ᱨ��������Ҫ��ǰ�����������
        if(text.length() == 0){
            return resultList;
        }

        // �ִ�
        List<Term> termList = HanLP.segment(text);
        // ��ȡ���е� 1.����/n ; 2.����/v ; 3.���ݴ�/a
        for (Term term : termList) {
            if(term.nature == Nature.n || term.nature == Nature.v || term.nature == Nature.a
                    || term.nature == Nature.vn){
                resultList.add(term.word);
            }
        }

        return resultList;
    }

    *//**
      ����������ת��Ϊ������������������� vectorA �� vectorB ��
     * @param wordListA : �ı� A �ĵ�������
     * @param wordListB : �ı� B �ĵ�������
     * @param vectorA   : �ı� A ת����Ϊ������ A
     * @param vectorB   : �ı� B ת����Ϊ������ B
     * @return vocabulary : �ʻ��
     *//*
    private static List<String> convertWordList2Vector(List<String> wordListA,List<String> wordListB,List<Double> vectorA,List<Double> vectorB){
        // �ʻ��
        List<String> vocabulary = new ArrayList<>();

        // ��ȡ�ʻ�� wordListA ��Ƶ�ʱ���ͬʱ�����ʻ��
        Map<String,Double> frequencyTableA = buildFrequencyTable(wordListA, vocabulary);

        // ��ȡ�ʻ�� wordListB ��Ƶ�ʱ���ͬʱ�����ʻ��
        Map<String,Double> frequencyTableB = buildFrequencyTable(wordListB, vocabulary);

        // ����Ƶ�ʱ�õ�����
        getWordVectorFromFrequencyTable(frequencyTableA,vectorA,vocabulary);
        getWordVectorFromFrequencyTable(frequencyTableB,vectorB,vocabulary);

        return vocabulary;
    }

    *//**
     * @param wordList����������
     * @param vocabulary: �ʻ��
     * @return Map<String,Double>: keyΪ���ʣ�valueΪƵ��
     *�����ʻ�� wordList ��Ƶ�ʱ���ͬʱ�����ʻ��
     *//*
    private static Map<String,Double> buildFrequencyTable(List<String> wordList,List<String> vocabulary){
        // �Ƚ���Ƶ����
        Map<String,Integer> countTable = new HashMap<>();
        for (String word : wordList) {
            if(countTable.containsKey(word)){
                countTable.put(word,countTable.get(word)+1);
            }
            else{
                countTable.put(word,1);
            }
            // �ʻ���������ظ�Ԫ�صģ�����ֻ�� vocabulary ��û�и�Ԫ��ʱ�ż���
            if(!vocabulary.contains(word)){
                vocabulary.add(word);
            }
        }
        // totalCount ���ڼ�¼�ʳ��ֵ��ܴ���
        int totalCount = wordList.size();
        // ��Ƶ����ת��ΪƵ�ʱ�
        Map<String,Double> frequencyTable = new HashMap<>();
        for (String key : countTable.keySet()) {
            frequencyTable.put(key,(double)countTable.get(key)/totalCount);
        }
        return frequencyTable;
    }

    *//**
     * @param frequencyTable : Ƶ�ʱ�
     * @param wordVector     : ת����Ĵ�����
     * @param vocabulary     : �ʻ��
     * ���ݴʻ����ı���Ƶ�ʱ�������������� wordVector �� vocabulary Ӧ����ͬά��
     *//*
    private static void getWordVectorFromFrequencyTable(Map<String,Double> frequencyTable,List<Double> wordVector,List<String> vocabulary){
        for (String word : vocabulary) {
            double value = 0.0;
            if(frequencyTable.containsKey(word)){
                value = frequencyTable.get(word);
            }
            wordVector.add(value);
        }
    }

    *//**
     *�������� A ������ B �ļн�����ֵ
     * @param vectorA   : ������ A
     * @param vectorB   : ������ B
     * @return
     *//*
    private static double countCosine(List<Double> vectorA,List<Double> vectorB){
        // �ֱ����������ƽ����
        double sqrtA = countSquareSum(vectorA);
        double sqrtB = countSquareSum(vectorB);

        // ���������ĵ��
        double dotProductResult = 0.0;
        for(int i = 0;i < vectorA.size();i++){
            dotProductResult += vectorA.get(i) * vectorB.get(i);
        }

        return dotProductResult/(sqrtA*sqrtB);
    }

    // ��������ƽ���͵Ŀ���
    private static double countSquareSum(List<Double> vector){
        double result = 0.0;
        for (Double value : vector) {
            result += value*value;
        }
        return Math.sqrt(result);
    }*/
}