/**
 * Copyright 2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.paoding.analysis.knife;

/**
 * Knife规定如何分解字符串成词语，并将分解成的词语告知{@link Collector}接口。
 * <p>
 * 
 * @author Zhiliang Wang [qieqie.wang@gmail.com]
 * 
 * @see Collector
 * @see Paoding
 * @see CJKKnife
 * @see CombinatoricsKnife
 * @see NumberKnife
 * @see LetterKnife
 * 
 * @since 1.0
 * 
 */
public interface Knife {
	/**
	 * 字符性质
	 * <P>
	 * 
	 * beef当前位置的字符不是Knife分词字符。 当没有存在POINT性质的字符时，下一个KNIFE从返回LIMIT性质的字符开始分词
	 */
	int LIMIT = -1;

	/**
	 * 字符性质
	 * <P>
	 * beef当前位置的字符是Knife分词字符
	 */
	int ASSIGNED = 1;

	/**
	 * 字符性质
	 * <P>
	 * beef当前位置的字符是Knife分词字符，但是下一个Knife从第一个返回POINT值的位置开始分词
	 * 如果没有POINT性质的字符，则返回LIMIT作为下一个Knife的分词开始位置
	 */
	int POINT = 0;

	/**
	 * 
	 * @param beef
	 *            要被分词的字符串
	 * @param history
	 *            Knife开始或有可能开始切词的始发位置，外部调用此方法判断Knife是否接受分词时应该设置history=index=当前的字符位置
	 * @param index
	 *            用以检视该位置字符是否能够被Knife分词，index >=history
	 * @return 字符性质 <br>
	 *         返回以下其中一个参数。当history==index时仅当返回ASSIGNED时，该Knife才有机会接收beef进行分词(即才有机会调用dissect方法)
	 * @see #LIMIT
	 * @see #ASSIGNED
	 * @see #POINT
	 */
	public int assignable(Beef beef, int history, int index);

	/**
	 * 分解词语，并将分解成的词语相关信息告知{@link Collector}接口。
	 * <p>
	 * 分解从beef的offset位置开始，直至可能的结束的位置，结束时返回具有特定意义的一个非0数字。<br>
	 * 
	 * @param collector
	 *            当分解到词语时，collector将被通知接收该词语
	 * @param beef
	 *            待分解的字符串，这个字符串可能是所要分解的全部字符串的一部分(比如文章中的某一部分)，当beef的最后一个字符为'\0'时，表示此次分解是文章最后一段。
	 * @param offset
	 *            字符串分解开始位置，即本此分解只需从beef.charAt(offset)开始
	 * @return 非0的整数，即正整数或负整数。<br>
	 *         正数时：表示此次分解到该结束位置(不包括该边界)，即此次成功分解了从offset到该位置的文本流。特别地，当其>=beef.lenght()表示已经把beef所有的词语分解完毕<br>
	 *         负数时：该负数的绝对值必须>=offset。这个绝对值表示此次成功分解了从offset到该绝对值的文本流，剩下的字符，该knife已经不能正确解析。(一般此时应该重新传入新的beef对象解析)
	 *         <p>
	 *         比如，有内容为"hello yang!"的文章，先读入8个字符"hello
	 *         ya"，此时分解后应该返回-5，表示正确解析到5这个位置，即"hello"，但必须读入新的字符然后再继续解析。
	 *         此时beef构造者就读入剩下的字符"ng!"并与前次剩下的" ya"构成串"
	 *         yang!"，这样才能继续解析，从而解析出"yang"!
	 * 
	 * 
	 */
	public int dissect(Collector collector, Beef beef, int offset);
}
