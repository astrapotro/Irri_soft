Imports System.IO

Public Class CONF


    Dim autoini As String

    Private Sub BGUARDAR_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles BGUARDAR.Click
        Dim guarda As StreamWriter
        'Lo he cambiado antes para linux, antes era c:\
        guarda = New StreamWriter("/home/mikel/DB.txt")
        guarda.WriteLine("ID:" + TBID.Text())
        guarda.WriteLine("HOSTR:" + TBHOSTR.Text())
        guarda.WriteLine("PUERTOR:" + TBPUERTOR.Text())
        guarda.WriteLine("DBR:" + TBDBR.Text())
        guarda.WriteLine("USUARIOR:" + TBUSUARIOR.Text())
        guarda.WriteLine("PASSR:" + TBPASSR.Text())
        guarda.WriteLine("TIEMPO:" + TBTIEMPO.Text())
        guarda.WriteLine("HOSTL:" + TBHOSTL.Text())
        guarda.WriteLine("PUERTOL:" + TBPUERTOL.Text())
        guarda.WriteLine("DBL:" + TBDBL.Text())
        guarda.WriteLine("USUARIOL:" + TBUSUARIOL.Text())
        guarda.WriteLine("PASSL:" + TBPASSL.Text())
        If TBAUTOINI.Checked = True Then
            guarda.WriteLine("AUTOINI:SI")
        ElseIf TBAUTOINI.Checked = False Then
            guarda.WriteLine("AUTOINI:NO")
        End If
        guarda.Close()
        Me.Close()

    End Sub


    Private Sub CONF_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load
        Dim linea As String
        Dim carga As StreamReader
        Try
            'Lo he cambiado antes para linux, antes era c:\
            carga = New StreamReader("/home/mikel/DB.txt")
            linea = carga.ReadLine()
            Do While Not (linea Is Nothing)

                If linea.Contains("ID:") Then
                    TBID.Text = linea.Remove(0, 3)
                ElseIf linea.Contains("HOSTR:") Then
                    TBHOSTR.Text = linea.Remove(0, 6)
                ElseIf linea.Contains("PUERTOR:") Then
                    TBPUERTOR.Text = linea.Remove(0, 8)
                ElseIf linea.Contains("DBR:") Then
                    TBDBR.Text = linea.Remove(0, 4)
                ElseIf linea.Contains("USUARIOR:") Then
                    TBUSUARIOR.Text = linea.Remove(0, 9)
                ElseIf linea.Contains("PASSR:") Then
                    TBPASSR.Text = linea.Remove(0, 6)
                ElseIf linea.Contains("TIEMPO:") Then
                    TBTIEMPO.Text = linea.Remove(0, 7)
                ElseIf linea.Contains("HOSTL:") Then
                    TBHOSTL.Text = linea.Remove(0, 6)
                ElseIf linea.Contains("PUERTOL:") Then
                    TBPUERTOL.Text = linea.Remove(0, 8)
                ElseIf linea.Contains("DBL:") Then
                    TBDBL.Text = linea.Remove(0, 4)
                ElseIf linea.Contains("USUARIOL:") Then
                    TBUSUARIOL.Text = linea.Remove(0, 9)
                ElseIf linea.Contains("PASSL:") Then
                    TBPASSL.Text = linea.Remove(0, 6)
                ElseIf linea.Contains("AUTOINI:") Then
                    autoini = linea.Remove(0, 8)
                    If autoini = "SI" Then
                        TBAUTOINI.Checked = True
                    ElseIf autoini = "NO" Then
                        TBAUTOINI.Checked = False
                    End If
                End If
                linea = carga.ReadLine()
            Loop
            carga.Close()
        Catch ex As Exception
            MsgBox("FALLO EN ARCHIVO DE CONFIGURACION. Puede que el archivo no exista.")
        End Try
    End Sub

End Class