package io.github.astrapi69.file.create;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import io.github.astrapi69.collection.list.ListFactory;

public class DirectoryStructureTestData
{

	/**
	 * create a collection with file infos with the following directory structure: <br>
	 * <br>
	 * | + app <br>
	 * | - + action <br>
	 * | - - - article.action.ts <br>
	 * | - + component <br>
	 * | - - + article <br>
	 * | - - - - article.component.css <br>
	 * | - - + board <br>
	 * | - - - - board.component.ts <br>
	 * | - - - - board.component.html <br>
	 * <br>
	 *
	 * @return the collection with file infos with the above directory structure
	 */
	public static Collection<FileContentInfo> newOtherTestData(String parentAbsolutePath)
	{
		Collection<FileContentInfo> fileInfos;
		FileContentInfo fileContentInfo;
		// new scenario...
		fileInfos = ListFactory.newArrayList();

		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath).name("action")
			.directory(true).build();
		fileInfos.add(fileContentInfo);
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath).name("component")
			.directory(true).build();
		fileInfos.add(fileContentInfo);
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/component")
			.name("article").directory(true).build();
		fileInfos.add(fileContentInfo);
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/component")
			.name("board").directory(true).build();
		fileInfos.add(fileContentInfo);

		String articleActionContent = "import { createAction } from '@ngrx/store';\n\n"
			+ "export const buy = createAction('[Buy Component] Buy');\n";
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/action")
			.name("article.action.ts")
			.content(articleActionContent.getBytes(StandardCharsets.UTF_8)).build();
		fileInfos.add(fileContentInfo);

		String articleComponentCssContent = "";
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/component/article")
			.name("article.component.css")
			.content(articleComponentCssContent.getBytes(StandardCharsets.UTF_8)).build();
		fileInfos.add(fileContentInfo);

		String boardComponentTsContent = "import { Component } from '@angular/core';\n" + "\n"
			+ "@Component({\n" + "  selector: 'app-board',\n"
			+ "  templateUrl: './board.component.html',\n" + "})\n"
			+ "export class BoardComponent {\n" + "}";
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/component/board")
			.name("board.component.ts")
			.content(boardComponentTsContent.getBytes(StandardCharsets.UTF_8)).build();
		fileInfos.add(fileContentInfo);
		String boardComponentHtmlContent = "<div></div>";
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/component/board")
			.name("board.component.html")
			.content(boardComponentHtmlContent.getBytes(StandardCharsets.UTF_8)).build();
		fileInfos.add(fileContentInfo);

		return fileInfos;
	}

	/**
	 * create a collection with file infos with the following directory structure: <br>
	 * <br>
	 * |+ app <br>
	 * | - + action <br>
	 * | - - - app.action.ts <br>
	 * | - + component <br>
	 * | - - - home.component.ts <br>
	 * | - - - home.component.html <br>
	 * | - - + article <br>
	 * | - - - - article.component.ts <br>
	 * | - - - - article.component.html <br>
	 * <br>
	 *
	 * @return the collection with file infos with the above directory structure
	 */
	public static Collection<FileContentInfo> newTestData(String parentAbsolutePath)
	{
		Collection<FileContentInfo> fileInfos;
		FileContentInfo fileContentInfo;
		// new scenario...
		fileInfos = ListFactory.newArrayList();
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath).name("action")
			.directory(true).build();
		fileInfos.add(fileContentInfo);
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath).name("component")
			.directory(true).build();
		fileInfos.add(fileContentInfo);
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/component")
			.name("article").directory(true).build();
		fileInfos.add(fileContentInfo);

		String appActionContent = "import { createAction } from '@ngrx/store';\n\n"
			+ "export const multiply = createAction('[Math Component] Multiply');\n"
			+ "export const divide = createAction('[Math Component] Divide');\n";
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/action")
			.name("app.action.ts").content(appActionContent.getBytes(StandardCharsets.UTF_8))
			.build();
		fileInfos.add(fileContentInfo);
		String homeComponentTsContent = "import { Component } from '@angular/core';\n"
			+ "import { Store, select } from '@ngrx/store';\n"
			+ "import { Observable } from 'rxjs';\n"
			+ "import { multiply, divide } from '../app.action';\n" + "\n" + "@Component({\n"
			+ "  selector: 'app-home',\n" + "  templateUrl: './home.component.html',\n" + "})\n"
			+ "export class HomeComponent {\n" + "  value$: Observable<number>;\n" + "\n"
			+ "  constructor(private store: Store<{ count: number }>) {\n"
			+ "    this.count$ = store.pipe(select('value'));\n" + "  }\n" + "\n"
			+ "  multiply() {\n" + "    this.store.dispatch(multiply());\n" + "  }\n" + "\n"
			+ "  divide() {\n" + "    this.store.dispatch(divide());\n" + "  }\n" + "}";
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/component")
			.name("home.component.ts")
			.content(homeComponentTsContent.getBytes(StandardCharsets.UTF_8)).build();
		fileInfos.add(fileContentInfo);
		String homeComponentHtmlContent = "<button id=\"multiply\" (click)=\"multiply()\">Multiply</button>\n\n"
			+ "<div>Current Value: {{ value$ | async }}</div>\n\n"
			+ "<button id=\"divide\" (click)=\"divide()\">Divide</button>";
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/component")
			.name("home.component.html")
			.content(homeComponentHtmlContent.getBytes(StandardCharsets.UTF_8)).build();
		fileInfos.add(fileContentInfo);

		String articleComponentTsContent = "import { Component } from '@angular/core';\n" + "\n"
			+ "@Component({\n" + "  selector: 'app-article',\n"
			+ "  templateUrl: './article.component.html',\n" + "})\n"
			+ "export class ArticleComponent {\n" + "}";
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/component/article")
			.name("article.component.ts")
			.content(articleComponentTsContent.getBytes(StandardCharsets.UTF_8)).build();
		fileInfos.add(fileContentInfo);
		String articleComponentHtmlContent = "<div></div>";
		fileContentInfo = FileContentInfo.builder().path(parentAbsolutePath + "/component/article")
			.name("article.component.html")
			.content(articleComponentHtmlContent.getBytes(StandardCharsets.UTF_8)).build();
		fileInfos.add(fileContentInfo);
		return fileInfos;
	}

}
