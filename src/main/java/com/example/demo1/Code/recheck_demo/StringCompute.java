
package com.example.demo1.Code.recheck_demo;



import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.*;

public class StringCompute {

    /**
     * 计算两个字符串的相似度
     *
     * @param strA 字符串A
     * @param strB 字符串B
     * @return 相似度
     */
    public static double SimilarDegree(String strA, String strB) {

        String newStrA = removeSign(strA);
        String newStrB = removeSign(strB);

        //用较大的字符串长度作为分母，相似子串作为分子计算出字串相似度
        int temp = Math.max(newStrA.length(), newStrB.length());
        int temp2 = longestCommonSubstring(newStrA, newStrB).length();

        return temp2 * 1.0 / temp;
    }

    /**
     * 将字符串的所有数据依次写成一行
     *
     * @param str 目标字符串
     * @return 操作后的字符串
     */
    public static String removeSign(String str) {

        StringBuilder stringBuilder = new StringBuilder();

        //遍历str,依次保存str中的汉字、数字、字母到sb
        for (char item : str.toCharArray())
            if (charReg(item)) {
                stringBuilder.append(item);
            }
        return stringBuilder.toString();
    }

    /**
     * 判断字符是否是汉字、数字、字母(不对符号进行相似度对比)
     *
     * @param charValue 待判断字符
     * @return 判断结果
     */
    public static boolean charReg(char charValue) {
        return (charValue >= 0x4E00 && charValue <= 0X9FA5) || (charValue >= 'a' && charValue <= 'z')
                || (charValue >= 'A' && charValue <= 'Z') || (charValue >= '0' && charValue <= '9');
    }

    /**
     * 利用动态规划算法求公共子串，求得的子串不一定为连续子串
     *
     * @param strA 第一个字符串
     * @param strB 第二个字符串
     * @return 公共子串
     */
    public static String longestCommonSubstring(String strA, String strB) {

        char[] chars_strA = strA.toCharArray();
        char[] chars_strB = strB.toCharArray();
        int m = chars_strA.length;
        int n = chars_strB.length;

        /*
          初始化矩阵数据,matrix[0][0]=0，
          如果字符数组chars_strA和chars_strB的对应位相同,则matrix[i][j]的值为左上角的值加1，
          否则，matrix[i][j]的值等于左上方最近两个位置的较大值,矩阵中其余各点的值为0.
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
         矩阵中，如果matrix[m][n]的值不等于matrix[m-1][n]的值也不等于matrix[m][n-1]的值，
         则matrix[m][n]对应的字符为相似字符元，并将其存入result数组中。
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
     * 将重复率转换为百分数
     *
     * @param result 重复率
     * @return 重复率百分比
     */
    public static String similarityResult(double result) {
        return NumberFormat.getPercentInstance(new Locale("en ", "US ")).format(result);
    }

    /**
     * 余弦定理计算相似度 1
     * @param strA 字符串
     * @param strB 字符串
     * @return 相似度
     */
    public static double getSimilarity(String strA, String strB) {

        String doc1 = removeSign(strA);
        String doc2 = removeSign(strB);
        if (doc1.trim().length() > 0 && doc2.trim().length() > 0) {

            Map<Integer, int[]> AlgorithmMap = new HashMap<>();

            //将两个字符串中的中文字符以及出现的总数封装到，AlgorithmMap中
            for (int i = 0; i < doc1.length(); i++) {
                char d1 = doc1.charAt(i);
                if(isHanZi(d1)){//标点和数字不处理
                    int charIndex = getGB2312Id(d1);//保存字符对应的GB2312编码
                    if(charIndex != -1){
                        int[] fq = AlgorithmMap.get(charIndex);
                        if(fq != null && fq.length == 2){
                            fq[0]++;//已有该字符，加1
                        }else {
                            fq = new int[2];
                            fq[0] = 1;
                            fq[1] = 0;
                            AlgorithmMap.put(charIndex, fq);//新增字符入map
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

            return denominator / Math.sqrt(sqdoc1*sqdoc2);//余弦计算
        } else {
            throw new NullPointerException(" the Document is null or have not cahrs!!");
        }
    }

    public static boolean isHanZi(char ch) {
        // 判断是否汉字
        return (ch >= 0x4E00 && ch <= 0x9FA5);
        /*if (ch >= 0x4E00 && ch <= 0x9FA5) {//汉字
            return true;
        }else{
            String str = "" + ch;
            boolean isNum = str.matches("[0-9]+");
            return isNum;
        }*/
        /*if(Character.isLetterOrDigit(ch)){
            String str = "" + ch;
            if (str.matches("[0-9a-zA-Z\\u4e00-\\u9fa5]+")){//非乱码
                return true;
            }else return false;
        }else return false;*/
    }

    /**
     * 根据输入的Unicode字符，获取它的GB2312编码或者ascii编码，
     *
     * @param ch 输入的GB2312中文字符或者ASCII字符(128个)
     * @return ch在GB2312中的位置，-1表示该字符不认识
     */
    public static short getGB2312Id(char ch) {
        try {
            byte[] buffer = Character.toString(ch).getBytes("GB2312");
            if (buffer.length != 2) {
                // 正常情况下buffer应该是两个字节，否则说明ch不属于GB2312编码，故返回'?'，此时说明不认识该字符
                return -1;
            }
            int b0 = (int) (buffer[0] & 0x0FF) - 161; // 编码从A1开始，因此减去0xA1=161
            int b1 = (int) (buffer[1] & 0x0FF) - 161;
            return (short) (b0 * 94 + b1);// 第一个字符和最后一个字符没有汉字，因此每个区只收16*6-2=94个汉字
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return -1;
    }



    /**
     * 余弦定理计算重复度 2
     */
    /*public static double computeTxtSimilar(String txtLeft, String txtRight) {


        //所有文档的总词库
        List<String> totalWordList = new ArrayList<>();
        //计算文档的词频
        Map<String, Integer> leftWordCountMap = getWordCountMap(txtLeft, totalWordList);
        Map<String, Float> leftWordTfMap = calculateWordTf(leftWordCountMap);

        Map<String, Integer> rightWordCountMap = getWordCountMap(txtRight, totalWordList);
        Map<String, Float> rightWordTfMap = calculateWordTf(rightWordCountMap);

        //获取文档的特征值
        List<Float> leftFeature = getTxtFeature(totalWordList, leftWordTfMap);
        List<Float> rightFeature = getTxtFeature(totalWordList, rightWordTfMap);

        //计算文档对应特征值的平方和的平方根
        float leftVectorSqrt = calculateVectorSqrt(leftWordTfMap);
        float rightVectorSqrt = calculateVectorSqrt(rightWordTfMap);

        //根据余弦定理公式，计算余弦公式中的分子
        float result = getCosValue(leftFeature, rightFeature);

        //根据余弦定理计算两个文档的余弦值
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
            //取字数为两个字或两个字以上名词或动名词作为关键词
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

    //计算关键词词频
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

    //计算文档对应特征值的平方和的平方根
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
     *余弦定理计算重复度 3
     */
   /* public static Double getCosineSimilarity(String textA,String textB){
        // 从文本中提取出关键词数组
        List<String> wordListA = extractWordFromText(textA);
        List<String> wordListB = extractWordFromText(textB);

        List<Double> vectorA = new ArrayList<>();
        List<Double> vectorB = new ArrayList<>();
        // 将关键词数组转换为词向量并保存在 vectorA 和 vectorB 中
        convertWordList2Vector(wordListA,wordListB,vectorA,vectorB);

        // 计算向量夹角的余弦值
        double cosine = Double.parseDouble(String.format("%.4f",countCosine(vectorA,vectorB)));
        return cosine;
    }

    // 提取文本中有实意的词
    private static List<String> extractWordFromText(String text){
        // resultList 用于保存提取后的结果
        List<String> resultList = new ArrayList<>();

        // 当 text 为空字符串时，使用分词函数会报错，所以需要提前处理这种情况
        if(text.length() == 0){
            return resultList;
        }

        // 分词
        List<Term> termList = HanLP.segment(text);
        // 提取所有的 1.名词/n ; 2.动词/v ; 3.形容词/a
        for (Term term : termList) {
            if(term.nature == Nature.n || term.nature == Nature.v || term.nature == Nature.a
                    || term.nature == Nature.vn){
                resultList.add(term.word);
            }
        }

        return resultList;
    }

    *//**
      将单词数组转换为单词向量，结果保存在 vectorA 和 vectorB 里
     * @param wordListA : 文本 A 的单词数组
     * @param wordListB : 文本 B 的单词数组
     * @param vectorA   : 文本 A 转换成为的向量 A
     * @param vectorB   : 文本 B 转换成为的向量 B
     * @return vocabulary : 词汇表
     *//*
    private static List<String> convertWordList2Vector(List<String> wordListA,List<String> wordListB,List<Double> vectorA,List<Double> vectorB){
        // 词汇表
        List<String> vocabulary = new ArrayList<>();

        // 获取词汇表 wordListA 的频率表，并同时建立词汇表
        Map<String,Double> frequencyTableA = buildFrequencyTable(wordListA, vocabulary);

        // 获取词汇表 wordListB 的频率表，并同时建立词汇表
        Map<String,Double> frequencyTableB = buildFrequencyTable(wordListB, vocabulary);

        // 根据频率表得到向量
        getWordVectorFromFrequencyTable(frequencyTableA,vectorA,vocabulary);
        getWordVectorFromFrequencyTable(frequencyTableB,vectorB,vocabulary);

        return vocabulary;
    }

    *//**
     * @param wordList：单词数组
     * @param vocabulary: 词汇表
     * @return Map<String,Double>: key为单词，value为频率
     *建立词汇表 wordList 的频率表，并同时建立词汇表
     *//*
    private static Map<String,Double> buildFrequencyTable(List<String> wordList,List<String> vocabulary){
        // 先建立频数表
        Map<String,Integer> countTable = new HashMap<>();
        for (String word : wordList) {
            if(countTable.containsKey(word)){
                countTable.put(word,countTable.get(word)+1);
            }
            else{
                countTable.put(word,1);
            }
            // 词汇表中是无重复元素的，所以只在 vocabulary 中没有该元素时才加入
            if(!vocabulary.contains(word)){
                vocabulary.add(word);
            }
        }
        // totalCount 用于记录词出现的总次数
        int totalCount = wordList.size();
        // 将频数表转换为频率表
        Map<String,Double> frequencyTable = new HashMap<>();
        for (String key : countTable.keySet()) {
            frequencyTable.put(key,(double)countTable.get(key)/totalCount);
        }
        return frequencyTable;
    }

    *//**
     * @param frequencyTable : 频率表
     * @param wordVector     : 转换后的词向量
     * @param vocabulary     : 词汇表
     * 根据词汇表和文本的频率表计算词向量，最后 wordVector 和 vocabulary 应该是同维的
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
     *计算向量 A 和向量 B 的夹角余弦值
     * @param vectorA   : 词向量 A
     * @param vectorB   : 词向量 B
     * @return
     *//*
    private static double countCosine(List<Double> vectorA,List<Double> vectorB){
        // 分别计算向量的平方和
        double sqrtA = countSquareSum(vectorA);
        double sqrtB = countSquareSum(vectorB);

        // 计算向量的点积
        double dotProductResult = 0.0;
        for(int i = 0;i < vectorA.size();i++){
            dotProductResult += vectorA.get(i) * vectorB.get(i);
        }

        return dotProductResult/(sqrtA*sqrtB);
    }

    // 计算向量平方和的开方
    private static double countSquareSum(List<Double> vector){
        double result = 0.0;
        for (Double value : vector) {
            result += value*value;
        }
        return Math.sqrt(result);
    }*/
}