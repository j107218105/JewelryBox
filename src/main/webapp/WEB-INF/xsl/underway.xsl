<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		doctype-system="about:blank"
		indent="no"
		media-type="text/html"
	/>

	<xsl:import href="/common.xsl"/>

	<xsl:template match="/">
		<HTML dir="ltr" lang="zh-TW">
			<xsl:apply-templates/>
		</HTML>
	</xsl:template>

	<xsl:template match="document">
		<HEAD>
			<META charset="UTF-8"/>
			<META content="width=device-width,initial-scale=1.0" name="viewport"/>
			<LINK href="{@contextPath}/STYLE/default.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/someone.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/default.js"/>
			<TITLE>寶盒計畫 &#187; 進行中</TITLE>
		</HEAD>
		<BODY>
			<HEADER>
				<DIV class="fL">
					<A href="{@contextPath}/">寶盒計畫</A>
					<SPAN>&#187;</SPAN>
					<A>進行中</A>
				</DIV>
				<DIV class="fR">
					<A>
						<xsl:value-of select="@name"/>
					</A>
					<SPAN>&#187;</SPAN>
					<A href="{@contextPath}/bye.asp">登出</A>
				</DIV>
			</HEADER>
			<SECTION>
				<DIV class="contentWrapper">
					<FORM id="search" class="cF" action="{@contextPath}/relativePath/">
						<DIV class="fR">
							<A class="button fontAwesome paginate" title="第一頁" tabindex="1">&#xF049;</A>
							<A class="button fontAwesome paginate" title="上一頁" tabindex="1">&#xF048;</A>
							<SPAN style="margin:0 3px">
								<LABEL>
									<SPAN>每頁</SPAN>
									<INPUT maxlength="2" name="s" type="text" value="10"/>
									<SPAN>筆</SPAN>
								</LABEL>
								<SPAN>：</SPAN>
								<LABEL>
									<SPAN>第</SPAN>
									<SELECT name="p">
										<OPTION value="0" selected="">1</OPTION>
										<OPTION value="1">2</OPTION>
									</SELECT>
									<SPAN>/</SPAN>
									<SPAN>2</SPAN>
									<SPAN>頁</SPAN>
								</LABEL>
								<SPAN>(共</SPAN>
								<SPAN>11</SPAN>
								<SPAN>筆)</SPAN>
							</SPAN>
							<A class="button fontAwesome paginate" title="下一頁" tabindex="1">&#xF051;</A>
							<A class="button fontAwesome paginate" title="最後頁" tabindex="1">&#xF050;</A>
						</DIV>
					</FORM>
				</DIV>
			</SECTION>
			<DIV class="contentWrapper">
				<NAV id="navigator">
					<UL id="navParts">
						<LI class="openCart">
							<DIV class="bold">活動</DIV>
							<UL class="navChapters">
								<LI>
									<A href="{@contextPath}/available.asp">未開始</A>
								</LI>
								<LI>
									<A>進行中</A>
								</LI>
								<LI>
									<A href="{@contextPath}/finished.asp">已結束</A>
								</LI>
							</UL>
						</LI>
						<LI>
							<DIV class="bold">撥款</DIV>
							<UL class="navChapters">
								<LI>
									<A href="{@contextPath}/withdraw.asp">申請</A>
								</LI>
								<LI>
									<A href="{@contextPath}/statement.asp">歷程</A>
								</LI>
							</UL>
						</LI>
						<LI>
							<DIV class="bold">帳戶</DIV>
							<UL class="navChapters">
								<LI>
									<A href="{@contextPath}/account.asp">個人資料</A>
								</LI>
								<LI>
									<A href="{@contextPath}/score.asp">點數明細</A>
								</LI>
								<LI>
									<A href="{@contextPath}/feedback.asp">聯絡我們</A>
								</LI>
							</UL>
						</LI>
					</UL>
				</NAV>
				<MENU id="bashful" type="toolbar">
					<DIV id="shield">進行中</DIV>
				</MENU>
				<DIV id="pixelLine"/>
				<ARTICLE>
					<DIV>
						<xsl:apply-templates select="table"/>
					</DIV>
				</ARTICLE>
			</DIV>
		</BODY>
	</xsl:template>

	<xsl:template match="table">
		<TABLE class="find">
			<THEAD>
				<TR>
					<TH>活動名稱</TH>
					<TH>價值點數</TH>
					<TH>結束時間</TH>
					<TH>活動進行情況</TH>
				</TR>
			</THEAD>
			<TBODY>
				<xsl:for-each select="row">
					<TR>
						<xsl:if test="position()mod'2'='0'">
							<xsl:attribute name="class">odd</xsl:attribute>
						</xsl:if>
						<TD class="textAlignLeft">
							<xsl:value-of select="name"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:value-of select="score"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:value-of select="until"/>
						</TD>
						<TD>
							<xsl:value-of select="status"/>
						</TD>
					</TR>
				</xsl:for-each>
			</TBODY>
		</TABLE>
		<P>
			<BR/>
		</P>
	</xsl:template>

</xsl:stylesheet>