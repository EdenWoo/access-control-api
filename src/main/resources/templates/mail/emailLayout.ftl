[#macro emailLayout]
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Email</title>
</head>
<body style="background-color:#E6E6E6; margin: 0; padding: 0;">

<table style="width:100%;" align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td align="center" valign="top" style="background-color:#E6E6E6;" bgcolor="#E6E6E6;">

                        <table width="600" border="0" align="center" cellspacing="0" cellpadding="0">
                            <tr>
                                <td align="left" valign="top" bgcolor="#FFFFFF"
                                    style="background-color:#FFFFFF; position: relative">
                                    <a href="http://collinsonco.com/forex" target="_blank">
                                        <img src="${setting.emailHeadImage}" alt="${setting.name}" width="600" height="230">
                                    </a>
                                </td>
                            </tr>


                            <!-- this is content start -->

                            [#nested]

                            <!-- this is content end -->

                            <tr>
                                <td align="center" valign="top" height="120" bgcolor="#2D3E51"
                                    style="background-color:#2a2569;">
                                    <table width="480" border="0" cellspacing="0" cellpadding="0" align="center"
                                           style="margin-top: 10px">
                                        <tr>
                                            <td align="center" valign="top"
                                                style="color:#db692b; font-family:Arial, Helvetica, sans-serif; font-size:16px; font-weight: 600; padding-top: 10px;">
                                                CONTACT US<br/>
                                                <table style="width: 480px; text-align: left;  margin-top: 10px; font-family: Arial, sans-serif; font-size:10px; line-height:18px; font-weight: 400;">
                                                    <tr>
                                                        <td style="float: left; width: 170px;">
                                                            <table width="150" style="line-height: 17px;">
                                                                <tr>
                                                                    <td colspan="2"
                                                                        style="padding:5px 0; color: #ecf0f1;">
                                                                        <span style=" margin-top: 8px; font-family: Arial, sans-serif; font-size:11px; line-height:18px; font-weight: 600; margin-bottom: 10px">Phone:</span>
                                                                    </td>
                                                                </tr>

                                                                <tr>
                                                                    <td style="color:#ecf0f1;  font-size: 11px;">
                                                                        ${setting.companyPhone}
                                                                    </td>
                                                                </tr>
                                                            </table>

                                                        </td>
                                                        <td style="float: left; padding-left: 15px;">
                                                            <table width="130" style="line-height: 17px;">
                                                                <tr>
                                                                    <td colspan="2"
                                                                        style="padding:5px 0; color: #ecf0f1;">
                                                                        <span style=" margin-top: 8px; font-family: Arial, sans-serif; font-size:11px; line-height:18px; font-weight: 600; margin-bottom: 10px">Mobile: </span>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="color:#ecf0f1; font-size: 11px;">
                                                                        ${setting.companyMobile}
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td style="float: right; padding-left: 15px">
                                                            <table width="120" style="line-height: 17px;">
                                                                <tr>
                                                                    <td colspan="2"
                                                                        style="padding:5px 0; color: #ecf0f1;">
                                                                        <span style=" margin-top: 8px; font-family: Arial, sans-serif; font-size:11px; line-height:18px; font-weight: 600; margin-bottom: 10px">Email: </span>

                                                                    </td>
                                                                </tr>

                                                                <tr>
                                                                    <td style="color:#ecf0f1;  font-size: 11px;">
                                                                        <a href="mailto:info.ccfx@collinsonfx.com"
                                                                           style="color:#ecf0f1; text-decoration:none;">
                                                                            ${setting.companyEmail}
                                                                        </a>

                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>

                                </td>
                            </tr>
                            <tr>
                                <td align="left" valign="top" bgcolor="#2D3E51" style="background-color:#19193f;">
                                    <table width="600" border="0" cellspacing="0" cellpadding="0" align="center">
                                        <tr>
                                            <td colspan="2" align="center"
                                                style="background-color: #19193f; color: #ddd;font-family: Arial, sans-serif; font-size:13px; line-height:33px;">
                                                ${setting.copyright}
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

</table>
</body>
</html>

[/#macro]