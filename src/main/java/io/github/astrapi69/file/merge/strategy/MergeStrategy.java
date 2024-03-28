/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.file.merge.strategy;

/**
 * This enum class defines strategies for merge directories
 */
public enum MergeStrategy
{

	/**
	 * TARGET_AS_MASTER Strategy will do: <br>
	 * <br>
	 * if exists in source, but not in target -> copy from source to target and delete from
	 * source<br>
	 * <br>
	 * if exists in target, but not in source -> do nothing<br>
	 * <br>
	 * if exists in both, but content not equal -> try to merge if fail copy from source to target
	 * and delete from source <br>
	 * if exists in both, and content is equal -> leave target unchanged and delete from source
	 */
	TARGET_AS_MASTER,

	/**
	 * SOURCE_TO_TARGET Strategy will do: <br>
	 * <br>
	 * if exists in source, but not in target -> copy from source to target <br>
	 * if exists in target, but not in source -> do nothing <br>
	 * if exists in both, but content not equal -> try to merge if fail copy from source to target
	 * <br>
	 * if exists in both, and content is equal -> leave target unchanged<br>
	 */
	SOURCE_TO_TARGET;
}
