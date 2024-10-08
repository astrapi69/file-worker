/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
module io.github.astrapisixtynine.file.worker
{
	requires org.apache.commons.io;
	requires org.apache.commons.lang3;
	requires java.logging;
	requires io.github.astrapisixtynine.silly.io;
	requires io.github.astrapisixtynine.silly.strings;
	requires io.github.astrapisixtynine.silly.collection;
	requires io.github.astrapisixtynine.checksum.up;
	requires io.github.astrapisixtynine.crypt.api;
	requires io.github.astrapisixtynine.throwable;

	exports io.github.astrapi69.file;
	exports io.github.astrapi69.file.compare;
	exports io.github.astrapi69.file.compare.api;
	exports io.github.astrapi69.file.copy;
	exports io.github.astrapi69.file.create;
	exports io.github.astrapi69.file.csv;
	exports io.github.astrapi69.file.delete;
	exports io.github.astrapi69.file.exception;
	exports io.github.astrapi69.file.modify;
	exports io.github.astrapi69.file.modify.api;
	exports io.github.astrapi69.file.read;
	exports io.github.astrapi69.file.rename;
	exports io.github.astrapi69.file.search;
	exports io.github.astrapi69.file.sort;
	exports io.github.astrapi69.file.system;
	exports io.github.astrapi69.file.write;
}
