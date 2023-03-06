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
			<LINK href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/vader/jquery-ui.min.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/default.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/someone.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/i18n/jquery-ui-i18n.min.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/default.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/form.js"/>
			<SCRIPT src="{@contextPath}/register.js"/>
			<TITLE>寶盒計畫 &#187; 註冊</TITLE>
		</HEAD>
		<BODY>
			<HEADER>
				<DIV class="fL">
					<A href="{@contextPath}/">寶盒計畫</A>
					<SPAN>&#187;</SPAN>
					<A>註冊</A>
				</DIV>
			</HEADER>
			<SECTION>
				<DIV class="contentWrapper">
					<!--
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
					-->
				</DIV>
			</SECTION>
			<DIV class="contentWrapper">
				<NAV id="navigator">
					<UL id="navParts">
						<LI class="openCart">
							<DIV class="bold">活動</DIV>
							<UL class="navChapters">
								<LI>
									<!--<A href="{@contextPath}/available.asp">(含待確認)</A>-->
									<A>未開始</A>
								</LI>
								<LI>
									<A href="{@contextPath}/underway.asp">進行中</A>
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
									<A href="{@contextPath}/score.asp">點數</A>
								</LI>
							</UL>
						</LI>
					</UL>
				</NAV>
				<MENU id="bashful" type="toolbar">
					<DIV id="shield">註冊</DIV>
				</MENU>
				<DIV id="pixelLine"/>
				<ARTICLE>
					<DIV>
						<xsl:apply-templates select="form"/>
					</DIV>
				</ARTICLE>
			</DIV>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<FORM action="{@action}" method="post">
			<INPUT name="from" type="hidden" value="{from}"/>
			<xsl:if test="from='F'">
				<INPUT name="facebookId" type="hidden" value="{facebookId}"/>
			</xsl:if>
			<xsl:if test="from='G'">
				<INPUT name="googleId" type="hidden" value="{googleId}"/>
			</xsl:if>
			<FIELDSET>
				<LEGEND>有<SPAN class="must"></SPAN>者為必填或必選欄位</LEGEND>
				<TABLE class="fieldset">
					<CAPTION>
						<xsl:value-of select="errorMessage"/>
					</CAPTION>
					<TR>
						<TH class="must">
							<LABEL for="birthday">生日</LABEL>
						</TH>
						<TD>
							<INPUT class="dP bitstreamVeraSansMono" id="birthday" name="birthday" readonly="" type="text" value="{birthday}"/>
						</TD>
						<TD class="note">必填。</TD>
					</TR>
					<TR>
						<TH class="must">性別</TH>
						<TD>
							<LABEL>
								<INPUT name="gender" type="radio" value="false">
									<xsl:if test="gender='false'">
										<xsl:attribute name="checked"/>
									</xsl:if>
								</INPUT>
								<SPAN>女性</SPAN>
							</LABEL>
							<LABEL>
								<INPUT name="gender" type="radio" value="true">
									<xsl:if test="gender='true'">
										<xsl:attribute name="checked"/>
									</xsl:if>
								</INPUT>
								<SPAN>男性</SPAN>
							</LABEL>
						</TD>
						<TD class="note">必選。</TD>
					</TR>
					<TR>
						<TH class="must">
							<LABEL for="name">全名</LABEL>
						</TH>
						<TD>
							<INPUT id="name" name="name" type="text" value="{name}"/>
						</TD>
						<TD class="note">必填。</TD>
					</TR>
					<TR>
						<TH class="must">
							<LABEL for="email">電子郵件</LABEL>
						</TH>
						<TD>
							<INPUT class="bitstreamVeraSansMono" id="email" name="email" type="text" value="{email}"/>
						</TD>
						<TD class="note">必填且不得與現有會員重複。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="cellular">手機</LABEL>
						</TH>
						<TD>
							<INPUT class="bitstreamVeraSansMono" id="cellular" maxlength="10" name="cellular" type="text" value="{cellular}"/>
						</TD>
						<TD class="note">可稍後補填、不得與現有會員重複；未填寫將會導致您無法接受活動通知簡訊。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="occupation">職業</LABEL>
						</TH>
						<TD>
							<SELECT id="occupation" name="occupation">
								<OPTION value="">(職業)</OPTION>
								<xsl:apply-templates select="occupation/*"/>
							</SELECT>
						</TD>
						<TD class="note">可稍後補選；未選擇可能導致您無法符合活動參加資格。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="financialInstitution">金融機構</LABEL>
						</TH>
						<TD>
							<SELECT id="financialInstitution" name="financialInstitution">
								<OPTION value="">(金融機構)</OPTION>
								<xsl:apply-templates select="financialInstitution/*"/>
							</SELECT>
						</TD>
						<TD class="note">可稍後補選；未選擇將會導致您無法申請撥款。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="financialAccountHolder">金融帳戶戶名</LABEL>
						</TH>
						<TD>
							<INPUT class="bitstreamVeraSansMono" id="financialAccountHolder" name="financialAccountHolder" type="text" value="{financialAccountHolder}">
								<xsl:if test="not(financialInstitution/*/@selected)">
									<xsl:attribute name="disabled"/>
								</xsl:if>
							</INPUT>
						</TD>
						<TD class="note">可稍後補填；未填寫將會導致您無法申請撥款。</TD>
					</TR>
					<TR>
						<TH>
							<LABEL for="financialAccountNumber">金融帳戶號碼</LABEL>
						</TH>
						<TD>
							<INPUT class="bitstreamVeraSansMono" id="financialAccountNumber" name="financialAccountNumber" type="text" value="{financialAccountNumber}">
								<xsl:if test="not(financialInstitution/*/@selected)">
									<xsl:attribute name="disabled"/>
								</xsl:if>
							</INPUT>
						</TD>
						<TD class="note">可稍後補填；未填寫將會導致您無法申請撥款。</TD>
					</TR>
				</TABLE>
				<DIV class="cF">
					<INPUT class="fL" type="reset" value="取消"/>
					<INPUT class="fR" type="submit" value="確定"/>
				</DIV>
			</FIELDSET>
		</FORM>
	</xsl:template>

</xsl:stylesheet>